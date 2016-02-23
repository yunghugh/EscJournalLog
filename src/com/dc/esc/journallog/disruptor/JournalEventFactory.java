package com.dc.esc.journallog.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * EventFactory  用于初始化 Event
 * */
public class JournalEventFactory implements  EventFactory<JournalEvent > {

	@Override
	public JournalEvent newInstance() {
		return new JournalEvent();
	}

}
