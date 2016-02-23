package com.dc.esc.journallog.disruptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dc.esc.journallog.bean.JournalLog;
import com.lmax.disruptor.RingBuffer;

public class JournalEventProducer {
	private Log log = LogFactory.getLog(JournalEventProducer.class);
	private RingBuffer<JournalEvent> ringBuffer;
	  public JournalEventProducer(RingBuffer<JournalEvent> ringBuffer)
	    {
	        this.ringBuffer = ringBuffer;
	    }

	    public void onData(JournalLog journalLog)
	    {
	    	log.debug("放流水进队列-》"+journalLog.getESCFLOWNO());
	        long sequence = ringBuffer.next();  // Grab the next sequence
	        try
	        {
	        	JournalEvent event = ringBuffer.get(sequence); // Get the entry in the Disruptor
	                                                        // for the sequence
	            event.set(journalLog);  // Fill with data
	        }
	        finally
	        {
	            ringBuffer.publish(sequence);
	        }
	    }
}
