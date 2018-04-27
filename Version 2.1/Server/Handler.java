import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class Handler extends Thread {
	private Socket client;
	private final static String FOLDER_FOR_SENDING = "/Users/jiahaozhao/Desktop/";  
	
	public Handler(Socket client) {
		this.client = client;
	}
	
	public void run () {
		FileInputStream file_in = null;
	    BufferedInputStream br = null;
	    OutputStream os = null;
	    BufferedReader FileRquest = null;
	    String SendingFile;
	    try {
	    		FileRquest = new BufferedReader(new InputStreamReader(client.getInputStream(),"UTF-8"));
	    		SendingFile = FileRquest.readLine();
	    		File myFile = new File (FOLDER_FOR_SENDING + SendingFile);
	              byte [] mybytearray  = new byte [(int)myFile.length()];
	              file_in = new FileInputStream(myFile);
	              br = new BufferedInputStream(file_in);
	              br.read(mybytearray,0,mybytearray.length);
	              os = client.getOutputStream();
	              System.out.println("Sending " + FOLDER_FOR_SENDING + SendingFile + "(" + mybytearray.length + " bytes)");
	              os.write(mybytearray,0,mybytearray.length);
	              os.flush();
	              System.out.println("Done.");
	              client.close();
	    } catch (IOException e){
	    		
	    }
	}
}
