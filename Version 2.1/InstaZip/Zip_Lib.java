
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

public class Zip_Lib {
	Map<String, String> chunckMap = new HashMap<>();
	List<String> fileList = new ArrayList<>();
	String TargetDir;
	final String subfolder = "tmp/";
	String COMPRESSED;
	static int counter = 0;

	public Zip_Lib(String TargetDir) {
		this.TargetDir = TargetDir;
		COMPRESSED = TargetDir + subfolder;
	}

	public void toCompact() throws IOException {
		toFileList();
		BufferedReader bufferedreader = null;
		for (String fileName : fileList) {
			String chunckIndex = "";
			try {		
				fileName = TargetDir + fileName;
				FileReader file = new FileReader(fileName);
				bufferedreader = new BufferedReader(file);
				int c;
				String line = "";
				while((c = bufferedreader.read()) != -1) {
					line += (char) c;
					if((char) c == '\n') {
						if (chunckMap.containsKey(line)) {
							chunckIndex += " " + chunckMap.get(line);
						} else {
							String cName = Integer.toString(counter++);
							chunckMap.put(line, cName);
							WriteStringToFile(line, COMPRESSED + cName);
							chunckIndex += " " + cName;
						}
						line = "";
					}
				}
				if (!line.equals("")) {
					if (chunckMap.containsKey(line)) {
						chunckIndex += " " + chunckMap.get(line);
					} else {
						String cName = Integer.toString(counter++);
						chunckMap.put(line, cName);
						WriteStringToFile(line, COMPRESSED + cName);
						chunckIndex += " " + cName;
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

	public void recover() throws IOException {
		toFileList();
		System.out.println("Decompressed files: ");
		BufferedReader bufferedreader = null;
		for (String fileName : fileList) {
			fileName = TargetDir + fileName;
			try {					
				FileReader file = new FileReader(fileName);
				bufferedreader = new BufferedReader(file);
				String line = null;			
				String result = "";
				while ((line = bufferedreader.readLine()) != null) {
					String[] split = line.split(" ");
					for (String index : split) {
						if (!(index.equals(" ") || index.equals(""))) {
							result += ReadCompactFile(index);
						}
					}
				}
				WriteStringToFile(result, fileName);
			}
			catch(FileNotFoundException ex) {
				System.out.println("Unable to open file '" + fileName + "'");                
			}
			catch(IOException ex) {
				System.out.println("Error reading file '" + fileName + "'");                  
			}
			System.out.println(fileName);
		}
	}


	private void toFileList() {
		File f = new File(TargetDir);
		for (File fileEntry : f.listFiles()) {
			if (!fileEntry.isDirectory()) {
				String fileName = fileEntry.getName();
				System.out.println(fileName);
				fileList.add(fileName);
			}
		}
	}




	private String ReadCompactFile(String index) {
		String result = "";
		String fileName = COMPRESSED + index;
		BufferedReader bufferedreader = null;
		try {					
			FileReader file = new FileReader(fileName);
			bufferedreader = new BufferedReader(file);
			int c;
			while ((c = bufferedreader.read()) != -1) {
				result += (char) c;
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