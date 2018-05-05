package Dedu_V2_2;


import java.io.File;
import java.io.IOException;

public class Entrance{
	int stats = -1;
	// 0 represents store; 1 represents retrieve
	int newLock;
	// 0 represent to creat new locker
	// 1 represent existing locker
	private static String inputfile;
	private static String outputfile;

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Entrance newIns = new Entrance();
		if (args.length < 5) {
			throw new IllegalArgumentException("Not a valid argument");
		}
		if (args[0].equals("addfile")) {
			newIns.newLock = 0;
			newIns.stats = 0;
		} else if (args[0].equals("retrieve")){
			newIns.newLock = -1;
			newIns.stats = 1;
		} else if (args[0].equals("setnewlocker")) {
			newIns.newLock = 1;
			newIns.stats = 0;
		}
		else {
			throw new IllegalArgumentException("Not a valid argument: " + args[0]);
		}
		if (!args[1].equals("-file")) {
			throw new IllegalArgumentException("Please input -file <yourfilepath>!");
		}
		File inputDir = new File(args[2]);
		if (newIns.stats == 0 && !inputDir.exists()) {
			throw new IllegalArgumentException("File " +args[2] + "does not exist!");	
		} 
//		if (newIns.stats == 1) {
//			if (inputDir.isDirectory()) {
//				
//			}
//			if (args[2].charAt(args[2].length() - 1) == '/') {
//				inputDir.mkdir();
//				System.out.println("new Folder " + args[2] + " made.");
//			}			
//			
//		}
		if (!args[3].equals("-lock")) {
			throw new IllegalArgumentException("Please input -lock <lockerpath>!");
		}

		File lockDir = new File(args[4]);
		if (newIns.newLock == 1 && newIns.stats == 0) {
			if (lockDir.exists()) {
				throw new  IllegalArgumentException(args[4] + " already exists!");
			} else {
				lockDir.mkdir();
				System.out.println("Putting file " + args[2] + "into new locker " + args[4] + "...");
			}
			newIns.outputfile = args[4];
		} else if (newIns.newLock == 0 && newIns.stats == 0) {
			if (!lockDir.exists() || !lockDir.isDirectory()) {
				throw new  IllegalArgumentException(args[4] + "  is not a locker");
			}
			lockDir = new File(args[4] + "tmp/CP.SPLITER");			
			if (!lockDir.exists()) {
				throw new IllegalArgumentException("Not a valid locker!");
			}
			newIns.outputfile = args[4];
			System.out.println("Putting file " + args[2] + "into existing locker " + args[4] + "...");
		}else if (newIns.stats == 1) {
			if (!lockDir.exists() || lockDir.isDirectory()|| !(new File(lockDir.getParent() + "/tmp/CP.SPLITER")).exists() ) {
				throw new  IllegalArgumentException(args[4] + "  is not a file existing in locker");
			}
			newIns.outputfile = lockDir.getParent() + "/";
		}		
		newIns.inputfile = args[2];		
		Zip_Lib lib = new Zip_Lib(newIns.inputfile, newIns.outputfile);
		if (newIns.stats == 0) {
			double t1 = System.nanoTime();
			lib.toCompact(newIns.newLock);
			double t2 = System.nanoTime();
			System.out.println(t2 - t1);
			//2.214386471E9
			//1.94424145E8

		} if (newIns.stats == 1) {
			double t3 = System.nanoTime();
			lib.recover((new File(args[4])).getName());
			double t4 = System.nanoTime();
			System.out.println(t4 - t3);
//			9.411701226E9
//			9.168170264E9			
		}
	}
}
