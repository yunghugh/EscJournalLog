package com.dc.esc.journallog.disruptor.fail;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dc.esc.ESCConfig;
import com.dc.esc.journallog.disruptor.JournalEvent;
import com.dc.esc.util.FileUtil;
import com.google.gson.Gson;
import com.lmax.disruptor.EventHandler;

/**
 * 处理队列里的消息
 * */
public class JournalFailEventHandler implements EventHandler<JournalEvent> {
	private static Log log = LogFactory.getLog(JournalFailEventHandler.class);

	@Override
	public void onEvent(JournalEvent je, long arg1, boolean arg2)
			throws Exception {
		// 处理
		String fileName = je.getData().getKey();
		if (log.isDebugEnabled()) {
			log.debug("收到一条失败流水消息：" + fileName);
		}
		String s = new Gson().toJson(je.getData());
		String path = ESCConfig.getConfig().getProperty(ESCConfig.JOURNALLOG_FILE_PATH);
		if (je.getData().getTRYCOUNT() >= ESCConfig.getConfig().getPropertyInt(ESCConfig.JOURNALLOG_DB_TRY)) {
			path = path+File.separator+"fail";
			FileUtil.write(s, path, fileName);

			if (log.isDebugEnabled()) {
				log.debug("写失败流水[" + fileName + "]到["+path+"]目录。");
			}
		} else {
			FileUtil.write(s, path, fileName);

			if (log.isDebugEnabled()) {
				log.debug("写失败流水[" + fileName + "]到["+path+"]目录。");
			}
		}

	}
}
