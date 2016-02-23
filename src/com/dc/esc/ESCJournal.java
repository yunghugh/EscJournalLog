package com.dc.esc;

import com.dc.esc.journallog.JournallogReTry;
import com.dc.esc.journallog.JournallogReceiver;
import com.dc.esc.journallog.disruptor.JournalWorkMain;
import com.dc.esc.journallog.disruptor.fail.JournalFailEventMain;
import com.dc.esc.journallog.hibernate.MyHibernate;
import com.dc.esc.log.LogReceiver;

public class ESCJournal {

	public static void main(String[] args) {
		//加载参数
		ESCConfig.getConfig();
		
		//初始化Hibernate
		new MyHibernate().getSessionFactory();
		//起流水队列
		JournalWorkMain.start();
		//起失败流水队列
		JournalFailEventMain.start();
		// 起TPC监听接收流水
		JournallogReceiver jr = new JournallogReceiver();
		Thread jthread = new Thread(jr);  
		jthread.start(); 
		// 起TPC监听接收日志
		LogReceiver lr = new LogReceiver();
		Thread lthread = new Thread(lr);  
		lthread.start(); 
		//起流水补登
		new JournallogReTry().start();
	}

}
