package deDuplicator;

import java.io.IOException;

public class deduplicator {
	static String folderName = "/Users/Iris/study/504/testFolder/";
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		lock(folderName);
	} 
	public static void lock(String FolderName) throws IOException, ClassNotFoundException {
		deDuplicate newIns = new deDuplicate();
		newIns.intoLocker(FolderName);
	}
	public static void recover(String fileName) throws IOException, ClassNotFoundException {
		deDuplicate newIns = new deDuplicate();
		newIns.recover(fileName);
	}

}
