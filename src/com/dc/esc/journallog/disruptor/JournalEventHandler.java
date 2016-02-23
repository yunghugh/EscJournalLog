package com.dc.esc.journallog.disruptor;

import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import com.dc.esc.journallog.disruptor.fail.JournalFailEventProducer;
import com.dc.esc.journallog.hibernate.MyHibernate;
import com.lmax.disruptor.EventHandler;

/**
 * 处理队列里的消息
 * */
public class JournalEventHandler implements EventHandler<JournalEvent> {
	private static Log log = LogFactory.getLog(JournalEventHandler.class);

	@Override
	public void onEvent(JournalEvent je, long arg1, boolean arg2)
			throws Exception {
		// 处理
		if (log.isDebugEnabled()) {
			log.debug("收到一条流水消息：" + je.getData().getESCFLOWNO());
		}
		try {
			// 流水入库
			Session session = new MyHibernate().getSessionFactory()
					.openSession();
			session.beginTransaction();
			je.getData().setTRANSSTAMP2(new Timestamp(System.currentTimeMillis()));
			session.save(je.getData());
			session.getTransaction().commit();
			session.close();
		} catch (Exception e) {
				if(log.isErrorEnabled()){
					log.error("流水["+je.getData().getESCFLOWNO()+"]入库失败！",e);
				}
				// 放流水进失败队列
				//入库次数++
				je.getData().setTRYCOUNT(je.getData().getTRYCOUNT()+1);
				JournalFailEventProducer.onData(je.getData());
		}

	}
}
