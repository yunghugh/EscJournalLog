package com.dc.esc.journallog.disruptor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dc.esc.ESCConfig;
import com.lmax.disruptor.FatalExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkerPool;

public class JournalWorkMain {
	private static Log log = LogFactory.getLog(JournalWorkMain.class);
	public static RingBuffer<JournalEvent> ringBuffer;

	public static void start() {
		int bufferSize = ESCConfig.getConfig().getPropertyInt(ESCConfig.JOURNALLOG_DISRUPTOR_BUFFERSIZE);
		if (log.isDebugEnabled()) {
			log.debug("启动[流水队列]bufferSize=["+bufferSize+"]");
		}
		ringBuffer = RingBuffer.createMultiProducer(new JournalEventFactory(),
				bufferSize);
		JournalWorkHandler[] handlers = new JournalWorkHandler[ESCConfig.getConfig().getPropertyInt(ESCConfig.JOURNALLOG_DISRUPTOR_CONSUMER)];
		for (int i = 0; i < handlers.length; i++) {
			handlers[i] = new JournalWorkHandler("" + i);
		}
		WorkerPool<JournalEvent> workerPool = new WorkerPool<JournalEvent>(
				ringBuffer, ringBuffer.newBarrier(),
				new FatalExceptionHandler(), new JournalWorkHandler("HUGH"));
		// 将WorkPool的工作序列集设置为ringBuffer的追踪序列。
		ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
		// 创建一个线程池用于执行Workhandler。
		Executor executor = Executors.newFixedThreadPool(10);
		// 启动WorkPool。
		workerPool.start(executor);
	}
}
