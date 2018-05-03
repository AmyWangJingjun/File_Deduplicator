package Dedu_V2_2;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private final static int PORT = 9527;  
	
	@SuppressWarnings("resource")
	public static void main (String [] args ) throws IOException {
		ServerSocket server = null;
		Socket client = null;
		server = new ServerSocket(PORT);
		while (true) {
			System.out.println("Waiting for connection...");
			client = server.accept();
			System.out.println("Accepted connection : " + client);
			new Handler(client).start();
		}
	}
}