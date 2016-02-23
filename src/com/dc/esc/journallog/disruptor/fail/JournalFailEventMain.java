package com.dc.esc.journallog.disruptor.fail;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dc.esc.ESCConfig;
import com.dc.esc.journallog.disruptor.JournalEvent;
import com.dc.esc.journallog.disruptor.JournalEventFactory;
import com.lmax.disruptor.dsl.Disruptor;

public class JournalFailEventMain {
	private static Log log = LogFactory.getLog(JournalFailEventMain.class);
	public static Disruptor<JournalEvent> disruptor = null;

	public static  void start() {

		// Specify the size of the ring buffer, must be power of 2.
		int ringBufferSize = ESCConfig.getConfig().getPropertyInt(ESCConfig.JOURNALLOG_DISRUPTOR_BUFFERSIZE);;
		if(log.isDebugEnabled()){
			log.debug("启动[失败流水队列]，BufferSize=["+ringBufferSize+"]");
		}
		// Executor that will be used to construct new threads for consumers
		Executor executor = Executors.newCachedThreadPool();
		JournalEventFactory eventFactory = new JournalEventFactory();
		// Construct the Disruptor
		disruptor = new Disruptor<JournalEvent>(
				eventFactory, ringBufferSize, executor);// Disruptor<>(JournalEvent::new,
														// bufferSize,
														// executor);

		// Connect the handler
		disruptor.handleEventsWith(new JournalFailEventHandler());// .handleEventsWith(JournalEventMain::handleEvent);

		// Start the Disruptor, starts all threads running
		disruptor.start();
	}
}
