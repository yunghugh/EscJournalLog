package test;


public class Agent implements Runnable {
	long id;
	public static void main(String[] args) {
		for(long l =1;l<=9;l++){
			new Thread(new Agent(l)).start();
		}
		
	}

	public Agent(long id1) {
		id = id1;
	}
	@Override
	public void run() {
//		TcpCleint.send(id);
		TcpCleintLog.send(id);
	}

}
