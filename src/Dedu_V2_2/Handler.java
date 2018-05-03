package Dedu_V2_2;

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
import java.io.RandomAccessFile;
import java.net.Socket;

public class Handler extends Thread {
	private Socket client;
	private final static String LOCKER_DIR = "/Users/Iris/Desktop/locker/";  
	private final static String LOCKER_DIR_TMP = "/Users/Iris/Desktop/locker/tmp/"; 
	
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
		    			WriteStringToFile(new String(charfile), LOCKER_DIR_TMP + filename);
		    		} catch (FileNotFoundException e) {
		    			File dir = new File(LOCKER_DIR_TMP);
		    			dir.mkdir();
		    			WriteStringToFile(new String(charfile), LOCKER_DIR_TMP + filename);
		    		} 
		    		File locker_valid = new File(LOCKER_DIR + Locker+"/");
		    		if(locker_valid.exists()) {
		    			new File(LOCKER_DIR_TMP + filename).renameTo(new File(LOCKER_DIR + Locker+"/"+filename));
		    			Entrance.main(new String[] {"addfile","-file", LOCKER_DIR + Locker+"/" + filename, "-lock", LOCKER_DIR + Locker+"/"});
		    		} else {
			    		Entrance.main(new String[] {"setnewlocker","-file", LOCKER_DIR_TMP + filename, "-lock", LOCKER_DIR + Locker+"/"});
		    		}
		    		new File(LOCKER_DIR_TMP + filename).delete();
	    		} else if (Request.equalsIgnoreCase("download")) {
	    			Locker = FileRquest.readLine();
	    			SendingFile = FileRquest.readLine();
		    		System.out.println(SendingFile);
		    		File myFile = new File (LOCKER_DIR + Locker + "/" +SendingFile);
		    		if (myFile.exists()) {
		    				Entrance.main(new String[] {"retrieve","-file", LOCKER_DIR_TMP, "-lock", LOCKER_DIR + Locker + "/" +SendingFile});
		    				File tmpFile = new File(LOCKER_DIR_TMP + SendingFile);
						byte [] mybytearray  = new byte [(int)tmpFile.length()];
						os = client.getOutputStream();
						try {
							file_in = new FileInputStream(tmpFile);
							br = new BufferedInputStream(file_in);
							br.read(mybytearray,0,mybytearray.length);					
							os.write((Integer.toString((int) tmpFile.length()) + "\n").getBytes("UTF-8"));
							os.write(mybytearray,0,mybytearray.length);
							os.flush();
							tmpFile.delete();
							System.out.println("Done.");
						} catch (FileNotFoundException e) {
							os.write("File does not exit\n".getBytes("UTF-8"));
						}
		    		}
				client.close();
	    		}
	    } catch (IOException e){
	    		e.printStackTrace();
	    } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
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
