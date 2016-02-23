package com.dc.esc;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ESCConfig {
	private static Log log = LogFactory.getLog(ESCConfig.class);
	/** 日志存放目录 */
	public static final String LOG_FILE_PATH = "log.file.path";
	/**  流水存放目录 */
	public static final String JOURNALLOG_FILE_PATH = "journallog.file.path";
	/** 日志TCP端口 */
	public static final String LOG_TCP_PORT = "log.tcp.port";
	/** 日志TCP连接数 */
	public static final String LOG_TCP_BACKLOG = "log.tcp.backlog";
	/** 日志TCP线程数 */
	public static final String LOG_TCP_THRNEADS = "log.tcp.thrneads";
	/** 流水TCP端口 */
	public static final String JOURNALLOG_TCP_PORT = "journallog.tcp.port";
	/** 流水TCP连接数 */
	public static final String JOURNALLOG_TCP_BACKLOG = "journallog.tcp.backlog";
	/** 流水TCP线程数 */
	public static final String JOURNALLOG_TCP_THRNEADS = "journallog.tcp.thrneads";
	/** 流水文件后缀 */
	public static final String JOURNALLOG_FILE_SUFFIX = "journallog.file.suffix";
	/** 流水补登尝试次数 */
	public static final String JOURNALLOG_DB_TRY = "journallog.db.try";
	/** 流水队列消费者数量 */
	public static final String JOURNALLOG_DISRUPTOR_CONSUMER = "journallog.disruptor.consumer";
	/** 流水队列大小 */
	public static final String JOURNALLOG_DISRUPTOR_BUFFERSIZE = "journallog.disruptor.buffersize";
	/** 失败流水队列大小 */
	public static final String JOURNALLOG_DISRUPTOR_FAIL_BUFFERSIZE = "journallog.disruptor.fail.buffersize";

	// private Properties props = new Properties();
	private HashMap<String, String> props = new HashMap<String, String>();

	private static ESCConfig escConfig = null;

	private ESCConfig() {
		init();
	}

	public static final ESCConfig getConfig() {
		if (escConfig == null)
			synchronized (ESCConfig.class) {
				if (escConfig == null)
					try {
						escConfig = new ESCConfig();
					} catch (Exception ex) {
						if (log.isErrorEnabled()) {
							log.error("Load ESC config error.", ex);
						}
					}
			}
		return escConfig;
	}

	private void init() {
		Properties properties = new Properties();
		try {
			InputStream is = ClassLoader.getSystemResourceAsStream(
					"config.properties");
			properties.load(is);
			is.close();
		} catch (Exception ex) {
			if (log.isErrorEnabled()) {
				log.error("Load system config error.", ex);
			}
		}
		Enumeration en = properties.keys();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			props.put(key, properties.getProperty(key));
		}
		if(log.isDebugEnabled()){
			log.debug("加载配置参数：\n"+props);
		}
	}

	public String getProperty(String name) {
		return props.get(name);
	}

	/**
	 * 获取布尔型配置参数
	 * */
	public Boolean getPropertyBoolean(String name) {
		return Boolean.valueOf(getProperty(name));
	}

	/**
	 * 获取布尔型配置参数
	 * */
	public int getPropertyInt(String name) {
		return Integer.valueOf(getProperty(name));
	}
}
