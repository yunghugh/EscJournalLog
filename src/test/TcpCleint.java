package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dc.esc.journallog.bean.JournalLog;
import com.google.gson.Gson;

public class TcpCleint {
	public static void send(long l) {
		String server = "127.0.0.1"; // 服务器IP
		// 发送的数据

		String test = "Flow-00000000000jjjjjjjjjjjjjjjjjjjj00000-"+l;
		List list = new ArrayList();
		for(int i=1;i<=10;i++){
			JournalLog jl = new JournalLog();
			jl.setTERMINALNO("ESC-HUGH_TERM0001");
			jl.setESCFLOWNO(test+"-"+i);
			jl.setTRANSSTAMP1(new Timestamp(System.currentTimeMillis()));
			
			list.add(jl);

			JournalLog jl2 = new JournalLog();
			jl2.setTERMINALNO("ESC-HUGH_TERM0002");
			jl2.setESCFLOWNO(test+"-"+i);
			jl2.setTRANSSTAMP1(new Timestamp(System.currentTimeMillis()));
			
			list.add(jl2);
		}
		
		
		byte[] recData = new byte[100];// 接收数据缓冲
		int servPort = 12315;// 端口
		try {
			Socket socket = new Socket(server, servPort);
//			System.out.println("send:"+test);
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			out.write(new Gson().toJson(list).getBytes());
			out.flush();
			socket.shutdownOutput();
			byte[] b = new byte[1];
			in.read(b);
//			System.out.println("!!!!!!!!!!!!!!!!!  >"+new String(b));
			
			out.close();
			in.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 发送
	}

}
