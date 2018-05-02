import java.io.*;
import java.net.*;

// store -file test -locker C
// load -file test -locker C

public class client {

	private static void WriteStringToFile(String result, String fileDir) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(fileDir));
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
	public static void main(String[] args) throws IOException {
		Socket server = null;
		FileInputStream file_in = null;
	    BufferedInputStream br = null;
	    OutputStream os = null;
		String userInput;
		String filename = null;
		InputStream in = null;
		DataInputStream dis = null;
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		
	    try {
	    		while ((userInput = stdIn.readLine()) != null) {
//	    			System.out.println(userInput);
		    		String[] command = userInput.split(" ");
		    		String fileDir = (command[1].equalsIgnoreCase("-file")) ? command[2] : command[4];
    				String Locker = (command[1].equalsIgnoreCase("-locker")) ? command[2] : command[4];
    				String[] Dir = fileDir.split("/");
    				filename = Dir[Dir.length - 1];
	    			if (command[0].equalsIgnoreCase("addfile")) {
//	    				server = new Socket("localhost", 9527);
	    				server = new Socket(args[0], 9527);
	    				os = server.getOutputStream();
	    				try {
		    				File myFile = new File (fileDir);
		    				file_in = new FileInputStream(myFile);
		    				
		    				os.write("upload\n".getBytes("UTF-8"));
				    		os.write((Locker+"\n").getBytes("UTF-8"));
				    		os.write((filename+"\n").getBytes("UTF-8"));
				    		os.flush();
				    		
				    		byte [] mybytearray  = new byte [1024];
			            int len = 0;   
			            while ((len = file_in.read(mybytearray)) != -1)  
			            {  
			                os.write(mybytearray,0,len);  
			            }
						server.close();
						System.out.println("Done.");
	    				} catch (FileNotFoundException e) {
	    					System.out.println("File does not exit.");
	    				}
	    			} else if (command[0].equalsIgnoreCase("retrieve")) {
//	    				server = new Socket("localhost", 9527);
	    				server = new Socket(args[0], 9527);
	    				os = server.getOutputStream();
	    				in = server.getInputStream();
	    				dis = new DataInputStream(in);
	    				String[] LockerDir = Locker.split("/");
	    				String RequestFile = LockerDir[LockerDir.length - 1];
	    				String RequestLocker = LockerDir[LockerDir.length - 2];
	    				os.write("download\n".getBytes("UTF-8"));
	    				os.write((RequestLocker+"\n").getBytes("UTF-8"));
	    				os.write((RequestFile+"\n").getBytes("UTF-8"));
	    				String sizeS = dis.readLine();
	    				
	    				if (!sizeS.equals("File does not exit")) {
		    		    		FileOutputStream fos = new FileOutputStream(fileDir + RequestFile);  
    	    			
		    	    			byte[] buf = new byte[1024];  
		    	    	        int len = 0;  
		    	    	        
		    	    	        while ((len = in.read(buf)) != -1)  
		    	    	        {  
		    	    	            fos.write(buf,0,len);  
		    	    	        } 
		    	    	        fos.close();  
		    	    	        in.close();
	    				} else {
	    					System.out.println(sizeS);
	    				}
	    			}
	    			server.close();
			}		
	    } catch (IOException e){
	    		
	    }
	}
	
}