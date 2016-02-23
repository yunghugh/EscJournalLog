package com.dc.esc.journallog.disruptor;

import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import com.dc.esc.journallog.disruptor.fail.JournalFailEventProducer;
import com.dc.esc.journallog.hibernate.MyHibernate;
import com.lmax.disruptor.WorkHandler;

/**
 * 处理队列里的消息
 * */
public class JournalWorkHandler implements WorkHandler<JournalEvent> {
	private static Log log = LogFactory.getLog(JournalWorkHandler.class);

	private String name;
	public JournalWorkHandler(String name) {
		this.name = name;
	}
	@Override
	public void onEvent(JournalEvent je)
			throws Exception {
		// 处理
		if (log.isDebugEnabled()) {
			log.debug("收到一条流水消息：" + je.getData().getESCFLOWNO());
		}
		System.out.println("WorkHandler["+name+"]处理事件"+je.getData()); 
		
		// 流水入库
		Session session = null;
		try {
			session= new MyHibernate().getSessionFactory()
					.openSession();
			session.beginTransaction();
			je.getData().setTRANSSTAMP2(new Timestamp(System.currentTimeMillis()));
			session.save(je.getData());
			session.getTransaction().commit();
			
		} catch (Exception e) {
				if(log.isErrorEnabled()){
					log.error("流水["+je.getData().getKey()+"]入库失败！");
				}
				//入库次数++
				je.getData().setTRYCOUNT(je.getData().getTRYCOUNT()+1);
				// 放流水进失败队列
				JournalFailEventProducer.onData(je.getData());
		}finally{
			if(null != session)
			session.close();
		}

	}

}
