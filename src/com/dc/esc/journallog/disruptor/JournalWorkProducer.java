package com.dc.esc.journallog.disruptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dc.esc.journallog.bean.JournalLog;
import com.lmax.disruptor.RingBuffer;

public class JournalWorkProducer {
	private Log log = LogFactory.getLog(JournalWorkProducer.class);
	public RingBuffer<JournalEvent> ringBuffer;
	private static JournalWorkProducer instance;

	public static synchronized JournalWorkProducer getInstance() {
		if (instance == null) {
			instance = new JournalWorkProducer();
		}
		return instance;
	}
	private JournalWorkProducer (){
		this.ringBuffer = JournalWorkMain.ringBuffer;
	}  
	public void onData(JournalLog journalLog) {
		long sequence = ringBuffer.next(); // Grab the next sequence
		try {
			JournalEvent event = ringBuffer.get(sequence); // Get the entry in
															// the Disruptor
															// for the sequence
			event.set(journalLog); // Fill with data
		} finally {
			ringBuffer.publish(sequence);
		}
		log.debug("放流水进队列["+sequence+"]->" + journalLog.getKey());
	}
}
