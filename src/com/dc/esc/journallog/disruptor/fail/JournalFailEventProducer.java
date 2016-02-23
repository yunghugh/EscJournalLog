package com.dc.esc.journallog.disruptor.fail;

import com.dc.esc.journallog.bean.JournalLog;
import com.dc.esc.journallog.disruptor.JournalEvent;

public class JournalFailEventProducer {

	    public static void onData(JournalLog journalLog)
	    {
	        long sequence = JournalFailEventMain.disruptor.getRingBuffer().next();  // Grab the next sequence
	        try
	        {
	        	JournalEvent event = JournalFailEventMain.disruptor.getRingBuffer().get(sequence); // Get the entry in the Disruptor
	                                                        // for the sequence
	            event.set(journalLog);  // Fill with data
	        }
	        finally
	        {
	        	JournalFailEventMain.disruptor.getRingBuffer().publish(sequence);
	        }
	    }
}
