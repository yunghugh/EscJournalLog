package com.dc.esc.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dc.esc.ESCConfig;

public class LogReceiver implements Runnable {



	private static Log log = LogFactory.getLog(LogReceiver.class);
	private int commport = 12316;
	private ServerSocket server;
	/** 最大连接数 */
	private int backlog = 100;
	private int nThrneads = 100;
	private ExecutorService exec;
	public LogReceiver() {
		this.commport = ESCConfig.getConfig().getPropertyInt(ESCConfig.LOG_TCP_PORT);
		this.backlog = ESCConfig.getConfig().getPropertyInt(ESCConfig.LOG_TCP_BACKLOG);
		this.nThrneads = ESCConfig.getConfig().getPropertyInt(ESCConfig.LOG_TCP_THRNEADS);
	}
	@Override
	public void run() {
		try {
			this.server = new ServerSocket(this.commport, this.backlog);
			this.exec = Executors.newFixedThreadPool(this.nThrneads);
			if (log.isDebugEnabled()) {
				log.debug("服务[LogReceiver]启动,端口[" + commport + "],并发数["+backlog+"],线程数["+nThrneads+"]......");
			}
			while (true) {
				Socket connection = this.server.accept();
				this.exec.execute(new HandleSocket(connection));
			}
		} catch (Exception e) {
			if (log.isErrorEnabled())
				log.error("服务[LogReceiver]捕获到异常！", e);
		}
	}

	class HandleSocket implements Runnable {
		private Socket socket;

		public HandleSocket(Socket skt) {
			this.socket = skt;
		}

		public void run() {
			if (log.isDebugEnabled()) {
				log.debug("服务[LogReceiver]收到一个日志..");
			}

			OutputStream os = null;
			try {
				os = socket.getOutputStream();
				receive(socket);
				os.write('S');
				os.flush();
				socket.shutdownOutput();
			} catch (IOException e) {
				if (log.isErrorEnabled()) {
					log.error("接收日志失败!", e);
				}
				// 接收失败
				try {
					os.write('F');
					os.flush();
				} catch (IOException e1) {
				}
			} finally {
				try {
					if (null != os)
						os.close();
					if (null != socket)
						socket.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 接收文件 流格式：文件头（终端号和文件名）长度（3位）+终端号#文件名+文件内容
	 */
	private void receive(Socket socket) throws IOException {
		InputStream inputStream = socket.getInputStream();
		byte[] receiveReply = null;
		int byteRead;
		byte[] tmp = new byte[10240];
		// 读取文件头
		byte[] headlen = new byte[3];
		inputStream.read(headlen);
		int len = Integer.valueOf(new String(headlen));
		byte[] head = new byte[len];
		inputStream.read(head);
		String heads = new String(head);
		String[] tf = heads.split("#");
		String path = ESCConfig.getConfig()
				.getProperty(ESCConfig.LOG_FILE_PATH);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		path = path + File.separator + date + File.separator + tf[0]
				+ File.separator;
		File logpath = new File(path);
		logpath.mkdirs();
		File file = new File(path + tf[1]);
		if (!file.exists()) {
			// 文件不存在创建
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		while ((byteRead = inputStream.read(tmp)) != -1) {
			// 写文件
			//如果tmp没写满
			fos.write(tmp,0,byteRead);
		}
		fos.close();
	}

}
