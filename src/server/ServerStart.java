package server;

public class ServerStart {
	
	public static void main(String[] args){
		Server server = Server.getInstance();
		server.init(6666);
		server.startListen(new ServerHandler());
	}
}
