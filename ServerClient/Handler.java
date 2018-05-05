import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class Handler extends Thread {
	private Socket client;
	
	//Uncomment and change the two directories to your own directories!!!
	//private final static String LOCKER = "/Users/jiahaozhao/Desktop/Locker/";
	//private final static String FOLDER_FOR_SENDING = "/Users/jiahaozhao/Desktop/Locker/tmp/";  
	
	public Handler(Socket client) {
		this.client = client;
	}
	
	@SuppressWarnings("deprecation")
	public void run () {
		FileInputStream file_in = null;
	    BufferedInputStream br = null;
	    OutputStream os = null;
	    BufferedReader FileRquest = null;
	    String Request;
	    String Locker;
	    String SendingFile;
	    String filename;
	    InputStream in = null;
	    DataInputStream dis = null;
	    File dir = new File(FOLDER_FOR_SENDING);
	    if(!dir.exists()) {
	    		dir.mkdirs();
	    }
	    try {
	    		in = client.getInputStream();
	    		dis = new DataInputStream(in);
	    		Request = dis.readLine();
	    		System.out.println(Request);

	    		if (Request.equalsIgnoreCase("upload")) {
	    			Locker = dis.readLine();
	    			filename = dis.readLine();
	    			
	    			FileOutputStream fos = new FileOutputStream(FOLDER_FOR_SENDING + filename);  
	    			
	    			byte[] buf = new byte[1024];  
	    	        int len = 0;  
	    	        
	    	        while ((len = in.read(buf)) != -1)  
	    	        {  
	    	            fos.write(buf,0,len);  
	    	        } 
	    	        fos.close();  
	    	        in.close(); 
		    		if (new File(LOCKER+Locker).exists()) {
		    			new File(FOLDER_FOR_SENDING + filename).renameTo(new File(LOCKER + Locker + "/" +filename));
		    			Entrance.main(new String[] {"addfile","-file", LOCKER + Locker + "/" + filename, "-lock", LOCKER + Locker + "/"});
		    		} else {
		    			Entrance.main(new String[] {"setnewlocker","-file", FOLDER_FOR_SENDING + filename, "-lock", LOCKER + Locker + "/"});
		    		}
		    		new File(FOLDER_FOR_SENDING + filename).delete();
	    		} else if (Request.equalsIgnoreCase("download")) {
	    			Locker = dis.readLine();
	    			SendingFile = dis.readLine();
		    		System.out.println(SendingFile);
				try {
					Entrance.main(new String[] {"retrieve","-file", FOLDER_FOR_SENDING, "-lock", LOCKER + Locker + "/" + SendingFile});
					File myFile = new File (FOLDER_FOR_SENDING + SendingFile);
			    		file_in = new FileInputStream(myFile);
			    		
			    		os = client.getOutputStream();				
					os.write((Integer.toString((int) myFile.length()) + "\n").getBytes("UTF-8"));
					os.flush();
					System.out.println("Done.");
					
			    		byte [] mybytearray  = new byte [1024];
		            int len = 0;   
		            while ((len = file_in.read(mybytearray)) != -1)  
		            {  
		                os.write(mybytearray,0,len);  
		            }
		            new File(FOLDER_FOR_SENDING + SendingFile).delete();
				} catch (FileNotFoundException e) {
					os.write("File does not exit\n".getBytes("UTF-8"));
				}
				client.close();
	    		}
	    } catch (IOException | ClassNotFoundException e){
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
