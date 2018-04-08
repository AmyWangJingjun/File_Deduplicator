package deDuplicator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class deDuplicate {
	Map<String, ListNode> allWords = new HashMap<>();
	Map<String, ListNode> heads = new HashMap<>();
	List<String> fileList = new ArrayList<>();
	String folderName;
	final String MAPNAME = "COMPACTMAP";

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		deDuplicate newIns = new deDuplicate();
//				newIns.intoLocker("/Users/Iris/study/504/testFolder/");
				newIns.recoverFile("test1_copy 2");
	} 

	public deDuplicate() {
		heads.put("\n", new ListNode("\n"));
		folderName = "/Users/Iris/study/504/testFolder/";
	}

	public void intoLocker(String folder) throws IOException {
		folderName = folder;
		File f = new File(folderName);
		toFileList(f);
		compact();
		storeBinaryFile();
	}

	public void recoverFile(String fileName) throws ClassNotFoundException, IOException {
		recoverMap();
		fileName = folderName + fileName;
		FileReader file = new FileReader(fileName);
		BufferedReader bufferedreader = new BufferedReader(file);
		String line = null;
		String result = "";
		while ((line = bufferedreader.readLine()) != null) {
			for (String word : line.split(" ")) {
				word = word + " ";
				if (heads.containsKey(word)) {
					ListNode cur = heads.get(word);
					while(cur != null) {
						result += cur.value;
						cur = cur.next;
					}
				}
			}
		}
		WriteStringToFile(result, fileName);	
	}

	private void recoverMap() throws IOException, ClassNotFoundException {
		Path fileLocation = Paths.get(folderName + MAPNAME);
		byte[] data = Files.readAllBytes(fileLocation);
		heads = (Map<String, ListNode>) recoverObject(data);
		//		recovertoFile(head, folderName +fileName);
	}

	private Object recoverObject(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ObjectInput in = null;
		Object result = null;
		try {
			in = new ObjectInputStream(bis);
			result = in.readObject(); 
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
			}
		}
		return result;
	}

	private void toFileList(File folder) {
		for (File fileEntry : folder.listFiles()) {
			if (!fileEntry.isDirectory()) {
				String fileName = fileEntry.getName();
				fileList.add(fileName);
			}
		}
	}

	private void compact() throws IOException {
		for (String fileName : fileList) {
			try {
				fileName = folderName + fileName;
				FileReader file = new FileReader(fileName);
				BufferedReader bufferedreader = new BufferedReader(file);
				String line = null;
				while ((line = bufferedreader.readLine()) != null) {
					go(line);
				}			
			}
			catch(FileNotFoundException ex) {
				System.out.println("Unable to open file '" + fileName + "'");                
			}
			catch(IOException ex) {
				System.out.println("Error reading file '" + fileName + "'");                  
			}
		}
		//store hashmap to disk
		writeBinaryFile(heads, folderName + MAPNAME);
	}

	private void storeBinaryFile() throws IOException{
		for (String fileName : fileList) {
			fileName = folderName + fileName;
			compacting(fileName);
		}		
	}

	private void writeBinaryFile(Object obj, String name) throws IOException{
		byte[] data = null;
		data = toByte(obj);
		FileOutputStream out = new FileOutputStream(name);
		out.write(data);
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private byte[] toByte(Object obj) throws IOException{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectOutput out = null;
		byte[] result = null;
		try {
			out = new ObjectOutputStream(stream);
			out.writeObject(obj);
			out.flush();
			result = stream.toByteArray();
		} finally {
			try {
				stream.close();
			} catch (IOException ex) {
			}
		}		
		return result;
	}

	//	public DataNode compacting(String filename) throws IOException {
	//		System.out.println(filename);
	//		FileReader file = new FileReader(filename);
	//		BufferedReader bufferedreader = new BufferedReader(file);
	//		String line = null;
	//		DataNode dummyHead = new DataNode();
	//		DataNode prev = dummyHead;
	//		while ((line = bufferedreader.readLine()) != null) {
	//			for (String word : line.split(" ")) {
	//				word = word + " ";
	//				if (heads.containsKey(word)) {
	//					DataNode newNode = new DataNode(heads.get(word));
	//					prev.next = newNode;
	//					prev = newNode;
	//				}
	//			}
	//			prev = addSymbol(prev, "\n");
	//		}
	//		return dummyHead.next;
	//	}
	public void compacting(String filename) throws IOException {
		FileReader file = new FileReader(filename);
		BufferedReader bufferedreader = new BufferedReader(file);
		String line = null;
		String result = "";
		while ((line = bufferedreader.readLine()) != null) {
			for (String word : line.split(" ")) {
				word = word + " ";
				if (heads.containsKey(word)) {
					result += word;
				}
			}
		}
		WriteStringToFile(result, filename);
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


	// fundamental method to de-duplicate using two hash maps and doubly linked list
	public void go(String words) {
		String[] split = words.split(" ");
		ListNode lastNode = null;
		for (int i = 0; i < split.length; i++) {
			String word = split[i] + " ";
			if (allWords.containsKey(word)) {
				//in this condition, current word has already appeared, and has linked with some node. check if last is same;
				ListNode cur = allWords.get(word);
				//????
				if (heads.containsKey(word)) {
					if (!(lastNode == null)) {
						lastNode.flag = true;
					}					
				} else {
					//no head
					if (cur.prev != lastNode) {
						cur.breakLink();
						heads.put(word, cur);
						if (!(lastNode == null)) {
							lastNode.flag = true;
							if (lastNode.next != null) {
								heads.put(lastNode.next.value, lastNode.next);
								lastNode.next.breakLink();							
							}
						}	
					}
				}
				lastNode = cur;
			} else {
				lastNode = addFreshWord(word, lastNode);				
			}
		}
		lastNode.flag = true;
		ListNode special = lastNode.next;
		if (special == null) {

		} else {
			special.breakLink();
			heads.put(special.value, special);
		}
		// set the end of each combo unlinkable;
	}

	public ListNode addFreshWord(String word, ListNode prev) {
		ListNode newNode = new ListNode(word);
		allWords.put(word, newNode);
		if (prev == null) {
			heads.put(word, newNode);
		} else {
			if (prev.flag == false) {
				newNode.link(prev);
				prev.flag = true;
			} else {
				heads.put(word, newNode);
				ListNode next = prev.next;
				if (next == null) {

				} else {
					next.breakLink();
					heads.put(next.value, next);
				}
			}
		}
		return newNode;
	}
}
