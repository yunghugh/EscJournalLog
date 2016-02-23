package com.dc.esc.journallog;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dc.esc.ESCConfig;
import com.dc.esc.journallog.bean.JournalLog;
import com.dc.esc.journallog.disruptor.JournalWorkProducer;
import com.dc.esc.util.JournallogFilter;
import com.google.gson.Gson;
import com.mchange.io.FileUtils;

public class JournallogReTry {
	private Log log = LogFactory.getLog(JournallogReTry.class);

	private static final int interval = 30000;
	public void start() {
		Timer timer = new Timer();
		timer.schedule(new MyTask(), 0, interval);
		if(log.isDebugEnabled()){
			log.debug("启动[流水补登]，执行间隔["+interval+"]");
		}
	}

	class MyTask extends TimerTask {

		@Override
		public void run() {
			// 补登流水
			String filepath = ESCConfig.getConfig().getProperty(ESCConfig.JOURNALLOG_FILE_PATH);
			File path = new File(filepath);
			FilenameFilter filter = new JournallogFilter(ESCConfig.getConfig().getProperty(ESCConfig.JOURNALLOG_FILE_SUFFIX));
			String[] filelist = path.list(filter);
			int filecount = filelist.length;
			int bat = filecount;
			if (log.isDebugEnabled()){
				log.debug("补登流水共["+filecount+"]条,本次["+bat+"]...");
			}
				for (int i = 0; i < bat; i++) {
					try {
					File readfile = new File(filepath + "\\" + filelist[i]);
					if (!readfile.isDirectory()) {
							String s = FileUtils.getContentsAsString(readfile);
							JournalLog jl = new Gson().fromJson(s,
									JournalLog.class);

							if(log.isDebugEnabled()){
								log.debug("补登流水["+jl.getESCFLOWNO()+"]");
							}
							// 放流水进队列
							JournalWorkProducer.getInstance().onData(jl);

						// 删文件
						readfile.delete();
					} 
				} catch (IOException e) {
					e.printStackTrace();
				}
				}
		}

	}

}