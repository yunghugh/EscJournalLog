package com.dc.esc.journallog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dc.esc.ESCConfig;
import com.dc.esc.journallog.bean.JournalLog;
import com.dc.esc.journallog.disruptor.JournalWorkProducer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 接收终端发送过来的流水，反序列化为对象后放进流水队列
 * */
public class JournallogReceiver implements Runnable {
	private static Log log = LogFactory.getLog(JournallogReceiver.class);
	private int commport = 12315;
	private ServerSocket server;
	/** 最大连接数 */
	private int backlog = 100;
	private int nThrneads = 100;
	private ExecutorService exec;

	public JournallogReceiver() {
		this.commport = ESCConfig.getConfig().getPropertyInt(ESCConfig.JOURNALLOG_TCP_PORT);
		this.backlog = ESCConfig.getConfig().getPropertyInt(ESCConfig.JOURNALLOG_TCP_BACKLOG);
		this.nThrneads = ESCConfig.getConfig().getPropertyInt(ESCConfig.JOURNALLOG_TCP_THRNEADS);
	}
	@Override
	public void run() {
		try {
			this.server = new ServerSocket(this.commport, this.backlog);
			this.exec = Executors.newFixedThreadPool(this.nThrneads);
			if (log.isDebugEnabled()) {
				log.debug("服务[JournallogReceiver]启动,端口[" + commport + "],并发数["+backlog+"],线程数["+nThrneads+"]......");
			}

			while (true) {
				Socket connection = this.server.accept();
				this.exec.execute(new HandleSocket(connection));
			}
		} catch (Exception e) {
			if (log.isErrorEnabled())
				log.error("服务[JournallogReceiver]捕获到异常！", e);
		}
	}

	class HandleSocket implements Runnable {
		private Socket socket;

		public HandleSocket(Socket skt) {
			this.socket = skt;
		}

		public void run() {
			OutputStream os = null;
			try {
				byte[] jlb = read(socket);
				String json = new String(jlb);
				os = socket.getOutputStream();
				log.debug("收到流水（json）：" + json);
				// 放流水进队列
				List<JournalLog> list = new Gson().fromJson(json,
						new TypeToken<List<JournalLog>>() {
						}.getType());
				for (JournalLog jl : list) {
					JournalWorkProducer.getInstance().onData(jl);
				}
				// JournalLog jl = new JournalLog();
				// jl.setTERMINALNO("ESC-HUGH_TERM0001");
				// jl.setESCFLOWNO(new String(jlb));
				// jl.setTRANSSTAMP1(new Timestamp(System.currentTimeMillis()));
				// journalEventProducer.onData(jl);

				// 通知Agent接收成功
				os.write('S');
				os.flush();

			} catch (Exception e) {
				if (log.isErrorEnabled()) {
					log.error("流水接收失败", e);
				}
				// 通知Agent接收失败
				try {
					os.write('F');
					os.flush();
				} catch (Exception e1) {
				}
			} finally {
				try {
					if (null != os)
						os.close();
					if (null != socket)
						socket.close();
				} catch (Exception e) {
				}
			}
		}

		/**
		 * 默认的Socket读取方式 注意 只适用于短连接
		 * 
		 * @param inputStream
		 * @return
		 * @throws IOException
		 */
		private byte[] read(Socket socket) throws IOException {
			InputStream inputStream = socket.getInputStream();
			byte[] receiveReply = null;
			int byteRead;
			byte[] tmp = new byte[1024];
			ByteArrayOutputStream baos = null;
			try {
				baos = new ByteArrayOutputStream();
				while ((byteRead = inputStream.read(tmp)) != -1) {
					baos.write(tmp, 0, byteRead);
				}

				receiveReply = baos.toByteArray();

			} finally {
				if (baos != null) {
					baos.close();
				}
				if (inputStream != null) {
					// inputStream.close();
				}
			}
			return receiveReply;
		}
	}

}
