import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class Handler extends Thread {
	private Socket client;
	private final static String FOLDER_FOR_SENDING = "/Users/jiahaozhao/Desktop/Locker/";  
	
	public Handler(Socket client) {
		this.client = client;
	}
	
	public void run () {
		FileInputStream file_in = null;
	    BufferedInputStream br = null;
	    OutputStream os = null;
	    BufferedReader FileRquest = null;
	    String Request;
	    String Locker;
	    String SendingFile;
	    String filename;
	    String result = "";
	    try {
	    		FileRquest = new BufferedReader(new InputStreamReader(client.getInputStream(),"UTF-8"));
	    		Request = FileRquest.readLine();
	    		if (Request.equalsIgnoreCase("upload")) {
	    			Locker = FileRquest.readLine();
	    			System.out.println(Locker);
	    			filename = FileRquest.readLine();
	    			Integer size = Integer.parseInt(FileRquest.readLine());
		    		System.out.println(size);
		    		char [] charfile = new char[size]; 		
		    		FileRquest.read(charfile);
		    		try {
		    			WriteStringToFile(new String(charfile), FOLDER_FOR_SENDING + Locker+"/" + filename);
		    		} catch (FileNotFoundException e) {
		    			File dir = new File(FOLDER_FOR_SENDING + Locker+"/");
		    			dir.mkdir();
		    			WriteStringToFile(new String(charfile), FOLDER_FOR_SENDING + Locker+"/" + filename);
		    		} 
		    		
	    		} else if (Request.equalsIgnoreCase("download")) {
	    			Locker = FileRquest.readLine();
	    			SendingFile = FileRquest.readLine();
		    		System.out.println(SendingFile);
		    		File myFile = new File (FOLDER_FOR_SENDING + Locker + "/" +SendingFile);
				byte [] mybytearray  = new byte [(int)myFile.length()];
				os = client.getOutputStream();
				try {
					file_in = new FileInputStream(myFile);
					br = new BufferedInputStream(file_in);
					br.read(mybytearray,0,mybytearray.length);					
					os.write((Integer.toString((int) myFile.length()) + "\n").getBytes("UTF-8"));
					os.write(mybytearray,0,mybytearray.length);
					os.flush();
					System.out.println("Done.");
				} catch (FileNotFoundException e) {
					os.write("File does not exit\n".getBytes("UTF-8"));
				}

				client.close();
	    		}
	    } catch (IOException e){
	    		e.printStackTrace();
	    } 
	}
	
	private void WriteStringToFile(String result, String fileName) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
		try {
			out.write(result); 
		}
		catch (IOException e)
		{
			System.out.println("Exception ");
		}
		finally
		{
			out.close();
		}
	} 
}
