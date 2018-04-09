package deDuplicator_V2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import deDuplicator.deDuplicate;

public class deduplicate {
	Map<String, String> chunckMap = new HashMap<>();
	List<String> fileList = new ArrayList<>();
	String folderName;
	final String subfolder = "COMPACTFOLDER/";
	String COMPACTFOLDERNAME;
	static int counter = 0;

	public deduplicate(String folder) {
		folderName = folder;
		COMPACTFOLDERNAME = folderName + subfolder;
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		deduplicate newIns = new deduplicate("/Users/Iris/study/504/testFolder/");
		// here need to replace folder name to a local one
		/**Notice: 
		 * Need to make a new directory called "COMPACTFOLDER/" in the folder that need to be deduplicates.
		 * To implement the duplicator, use "toCompact()" function to deduplicates the files
		 * To recover a file, run the "recover(_)" function, to recover a file that has been compiled before.
		 **/

				newIns.toCompact();
		//		newIns.recover("test1_copy_3");
	}

	public void toCompact() throws IOException {
		toFileList();
		for (String fileName : fileList) {
			String chunckIndex = " ";
			try {		
				fileName = folderName + fileName;
				FileReader file = new FileReader(fileName);
				BufferedReader bufferedreader = new BufferedReader(file);
				String line = null;
				while ((line = bufferedreader.readLine()) != null) {
					if (chunckMap.containsKey(line)) {
						chunckIndex += chunckMap.get(line) + " ";
					} else {
						String cName = Integer.toString(counter++);
						chunckMap.put(line, cName);
						WriteStringToFile(line, COMPACTFOLDERNAME + cName);
						chunckIndex += cName + " ";
					}
				}	

			}
			catch(FileNotFoundException ex) {
				System.out.println("Unable to open file '" + fileName + "'");                
			}
			catch(IOException ex) {
				System.out.println("Error reading file '" + fileName + "'");                  
			}
			WriteStringToFile(chunckIndex, fileName);

		}
	}

	public void recover(String fileName) throws IOException {
		fileName = folderName + fileName;
		String result = " ";
		try {					
			FileReader file = new FileReader(fileName);
			BufferedReader bufferedreader = new BufferedReader(file);
			String line = null;			
			while ((line = bufferedreader.readLine()) != null) {
				String[] split = line.split(" ");
				for (String index : split) {
					if (!(index.equals(" ") || index.equals(""))) {
						result += ReadCompactFile(index) + "\n";
					}
				}
			}
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");                
		}
		catch(IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");                  
		}
		WriteStringToFile(result, fileName);

	}


	private void toFileList() {
		File f = new File(folderName);
		for (File fileEntry : f.listFiles()) {
			if (!fileEntry.isDirectory()) {
				String fileName = fileEntry.getName();
				System.out.println(fileName);
				fileList.add(fileName);
			}
		}
	}




	private String ReadCompactFile(String index) {
		String result = " ";
		String fileName = COMPACTFOLDERNAME + index;
		try {					
			FileReader file = new FileReader(fileName);
			BufferedReader bufferedreader = new BufferedReader(file);
			String line = null;

			while ((line = bufferedreader.readLine()) != null) {
				result += line;
			}			
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");                
		}
		catch(IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");                  
		}
		return result;
	}


	private void WriteStringToFile(String result, String fileName) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
		try {
			out.write(result);  //Replace with the string 
			//you are trying to write
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

