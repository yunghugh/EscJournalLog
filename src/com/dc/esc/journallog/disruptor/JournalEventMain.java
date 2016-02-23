package com.dc.esc.journallog.disruptor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

public class JournalEventMain {
	private static Log log = LogFactory.getLog(JournalEventMain.class);
	public static Disruptor<JournalEvent> disruptor;

	public void start() {
		if(log.isDebugEnabled()){
			log.debug("启动[流水队列]");
		}
		// Executor that will be used to construct new threads for consumers
		Executor executor = Executors.newCachedThreadPool();

		// Specify the size of the ring buffer, must be power of 2.
		int ringBufferSize = 1024;
		if(log.isDebugEnabled()){
			log.debug("流水队列长度为["+ringBufferSize+"]");
		}
		JournalEventFactory eventFactory = new JournalEventFactory();
		// Construct the Disruptor
		disruptor = new Disruptor<JournalEvent>(
				eventFactory, 
				ringBufferSize, 
				executor,
				ProducerType.SINGLE,
				new BlockingWaitStrategy());

		// Connect the handler
		disruptor.handleEventsWith(new JournalEventHandler());// .handleEventsWith(JournalEventMain::handleEvent);

		// Start the Disruptor, starts all threads running
		disruptor.start();
	}
}
