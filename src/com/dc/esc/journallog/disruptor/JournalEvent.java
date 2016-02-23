package com.dc.esc.journallog.disruptor;

import com.dc.esc.journallog.bean.JournalLog;

/**
 * Event 用于装载数据
 * */
public class JournalEvent {
	private JournalLog journalLog;

	public void set(JournalLog jl) {
		this.journalLog = jl;
	}
	public JournalLog getData(){
		return journalLog;
	}
}
