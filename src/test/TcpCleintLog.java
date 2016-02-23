package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TcpCleintLog {
	public static void send(long id) {
		String server = "127.0.0.1"; // 服务器IP
		// 发送的数据

		String test = "030TERM"+id+"#log.20160702hhhhhhhh.log文件内容kkkkkkkkkkkkkkkkkkk\nkkddddddddddddddddddddddddddddd\nooooooooooooooooooooooooooooooooooooooooooooooooooo";
		List list = new ArrayList();
		byte[] recData = new byte[100];// 接收数据缓冲
		int servPort = 12316;// 端口
		try {
			Socket socket = new Socket(server, servPort);
			System.out.println("send:"+test);
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			out.write(test.getBytes());
			out.flush();
			socket.shutdownOutput();
			byte[] b = new byte[1];
			in.read(b);
			System.out.println("!!!!!!!!!!!!!!!!!  >"+new String(b));
			
			out.close();
			in.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 发送
	}

	public static void main(String[] args) {
		send(1);
	}
}
