import java.io.*;
import java.net.*;

// store -file test -locker C
// load -file test -locker C

public class client {
	private static String  location = "/Users/jiahaozhao/Desktop/upload/";
	private static void WriteStringToFile(String result, String fileName) throws IOException {
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
	public static void main(String[] args) throws IOException {
		Socket server = null;
		FileInputStream file_in = null;
	    BufferedInputStream br = null;
	    OutputStream os = null;
		String userInput;
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
	    try {
	    		while ((userInput = stdIn.readLine()) != null) {
	    			System.out.println(userInput);
		    		String[] command = userInput.split(" ");
		    		String filename = (command[1].equalsIgnoreCase("-file")) ? command[2] : command[4];
    				String Locker = (command[1].equalsIgnoreCase("-locker")) ? command[2] : command[4];
	    			if (command[0].equalsIgnoreCase("store")) {
	    				server = new Socket("localhost", 9527);
	    				os = server.getOutputStream();
	    				try {
		    				File myFile = new File (location + filename);
		    				
		    				os.write("upload\n".getBytes("UTF-8"));
				    		os.write((Locker+"\n").getBytes("UTF-8"));
				    		os.write((filename+"\n").getBytes("UTF-8"));
				    		os.write((Integer.toString((int) myFile.length()) + "\n").getBytes("UTF-8"));
				    		
				    		byte [] mybytearray  = new byte [(int)myFile.length()];
						file_in = new FileInputStream(myFile);
						br = new BufferedInputStream(file_in);
						br.read(mybytearray,0,mybytearray.length);
						os.write(mybytearray,0,mybytearray.length);
						os.flush();
						server.close();
						System.out.println("Done.");
	    				} catch (FileNotFoundException e) {
	    					System.out.println("File does not exit.");
	    				}
	    			} else if (command[0].equalsIgnoreCase("load")) {
	    				server = new Socket("localhost", 9527);
	    				os = server.getOutputStream();
	    				BufferedReader FileRquest = new BufferedReader(new InputStreamReader(server.getInputStream(),"UTF-8"));
	    				os.write("download\n".getBytes("UTF-8"));
	    				os.write((Locker+"\n").getBytes("UTF-8"));
	    				os.write((filename+"\n").getBytes("UTF-8"));
	    				String sizeS = FileRquest.readLine();
	    				if (!sizeS.equals("File does not exit")) {
		    	    			Integer size = Integer.parseInt(sizeS);
		    		    		char [] charfile = new char[size];
		    		    		FileRquest.read(charfile);
		    		    		WriteStringToFile(new String(charfile), "/Users/jiahaozhao/Desktop/Downloaded/"+filename);
	    				} else {
	    					System.out.println(sizeS);
	    				}
	    			}
			}
			server.close();
	    } catch (IOException e){
	    		
	    }
	}
	
}
