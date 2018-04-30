package Dedu_V2_2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

public class Zip_Lib {
	Map<String, String> chunckMap = new HashMap<>();
	Map<String, Long> tokenMap = new HashMap<>();
	List<String> fileList = new ArrayList<>();
	String TargetDir;
	String LockerDir;
	String Spliter;
	final String subfolder = "tmp/";
	final String MAP = "mp";
	String COMPRESSED;
	int counter = 0;

	public Zip_Lib(String TargetDir, String LockerDir) {
		this.LockerDir = LockerDir;
		this.TargetDir = TargetDir;
		COMPRESSED = LockerDir + subfolder;
	}

	public void toCompact(int flag) throws IOException {	
		if (flag == 0) {
			toFileList(COMPRESSED);
			List<String> tmplist = fileList;
			fileList = new ArrayList<>();
			for (String file : tmplist) {
				if (!file.equals("CP.SPLITER")) {
					String cur = new String(Files.readAllBytes(Paths.get(COMPRESSED + file)), StandardCharsets.UTF_8);
					chunckMap.put(cur, file);
				}
			}
		} 
		if ((new File(TargetDir)).isDirectory()) {
			// input is a folder
			toFileList(TargetDir);	
			System.out.println(fileList);
			for (int i = 0; i < fileList.size(); i++) {
				getCompressed(fileList.get(i), flag, 1);
				flag = 0;
			}
		} else {
			// input is a single file
			getCompressed((new File(TargetDir)).getName(), flag, 0);
		}
		displayStorage(LockerDir);
	}

	public void recover(String mode) throws IOException {
		System.out.println("Decompressed files: ");
		BufferedReader bufferedreader = null;
		if (mode.equals("all")) {
			toFileList(LockerDir);
			for (String fileName : fileList) {
				deCompressed (LockerDir + fileName);
			}
		} else {
			deCompressed (LockerDir + mode);
		}
	}
	
	private void deCompressed (String fileName) throws IOException {
		String originFile = null;
		if ((new File(TargetDir)).isDirectory()) {
			originFile = TargetDir + (new File(fileName)).getName();
		} else {
			originFile = TargetDir;
		}					
			FileReader file = new FileReader(fileName);
			BufferedReader bufferedreader = new BufferedReader(file);
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
			WriteStringToFile(result, originFile);
		System.out.println("Successfully retrieve file " + fileName);
	}

	private void getCompressed(String file, int flag, int mode) throws IOException {
		String fileName = file;
		String chunckIndex = "";
		String outputFile = LockerDir + fileName;
		String inputFile = null;
		if (mode == 1) {
			inputFile = TargetDir + fileName;
		} else {
			inputFile = TargetDir;
		}
		String curfile = new String(Files.readAllBytes(Paths.get(inputFile)), StandardCharsets.UTF_8);
		if (flag == 1) {
			// new locker, need to set hashmap and .spliter file
			File newdir = new File(COMPRESSED);
			newdir.mkdirs();
			Spliter = setSpliter(inputFile);
		} else {
			Spliter =  new String(Files.readAllBytes(Paths.get(COMPRESSED + "CP.SPLITER")), StandardCharsets.UTF_8);
		}
		System.out.println(Spliter);
		int len = Spliter.length();
		String cName = null;
		String substr = null;
		int prev = 0;
		int index = curfile.indexOf(Spliter, prev);

		while (index != -1) {
			substr = curfile.substring(prev, index + len);
			if (chunckMap.containsKey(substr)) {
				chunckIndex += chunckMap.get(substr) + " ";
			} else {
				cName = Integer.toString(counter++);
				chunckMap.put(substr, cName);
				WriteStringToFile(substr, COMPRESSED + cName);
				chunckIndex += cName + " ";
			}
			prev = index + len;
			index = curfile.indexOf(Spliter, prev);	
		}
		if (prev < curfile.length()) {
			substr = curfile.substring(prev, curfile.length());
			if (chunckMap.containsKey(substr)) {
				chunckIndex += chunckMap.get(substr) + " ";
			} else {
				cName = Integer.toString(counter++);
				chunckMap.put(substr, cName);
				WriteStringToFile(substr, COMPRESSED + cName);
				chunckIndex += cName;
			}
		}
		WriteStringToFile(chunckIndex,outputFile );
		
	}


	private String setSpliter(String filename) throws IOException {
		String fileContents =  new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
		StringTokenizer tokenizer = new StringTokenizer(fileContents);
		while(tokenizer.hasMoreTokens()) {
			String cur = tokenizer.nextToken();
			if (tokenMap.containsKey(cur)) {
				tokenMap.put(cur, tokenMap.get(cur) + 1);
			} else {
				tokenMap.put(cur, (long) 1);
			}
		}
		String result =  findMostFreq();
		WriteStringToFile(result, COMPRESSED + "CP.SPLITER" );
		return result;
	}

	private String findMostFreq() {
		String result = null;
		for (Entry<String, Long> entry: tokenMap.entrySet()) {
			if (entry.getValue() > 100 && entry.getValue() < 1000) {
				System.out.println(entry.getValue());
				result = entry.getKey();
				break;
			}
		}
		return result;
	}

	public String displayStorage(String dir) {
		System.out.println(dir);
		File f = new File(dir);
		double lengthInByte = folderSize(f);
		double lengthInKB = (double)lengthInByte / 1024;
		double lengthInMB = (double)lengthInKB / 1024;
		System.out.println(lengthInMB);
		if (lengthInMB > 1) {
			String MB = String.format("Current locker storage use is %.2f MB.", lengthInMB);
			return MB;
		} else if (lengthInKB > 1) {
			String KB = String.format("Current locker storage use is %.2f KB.", lengthInKB);
			return KB;
		} else {
			String Bt = String.format("Current locker storage use is %f Bytes.", lengthInKB);;
			return Bt;
		}
	}
	public static long folderSize(File directory) {
		long length = 0;
		for (File file : directory.listFiles()) {
			if (file.isFile())
				length += file.length();
			else
				length += folderSize(file);
		}
		return length;
	}



	private void toFileList(String curDir) {
		File f = new File(curDir);
		for (File fileEntry : f.listFiles()) {
			if (!fileEntry.isDirectory()) {
				String fileName = fileEntry.getName();
				System.out.println(fileName);
				if (fileName.charAt(0) != '.') {
					fileList.add(fileName);
				}				
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