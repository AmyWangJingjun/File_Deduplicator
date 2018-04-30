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
		} else if (args[0].equals("Retrieve")){
			newIns.newLock = -1;
			newIns.stats = 1;
		} else if (args[0].equals("setnewlocker")) {
			newIns.newLock = 0;
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
		if (newIns.stats == 1 && !inputDir.exists()) {
			inputDir.mkdir();
			System.out.println("new Folder " + args[2] + " made.");
		}
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
		} else if (newIns.newLock == 0) {
			if (!lockDir.exists()) {
				throw new  IllegalArgumentException(args[4] + "  is not a locker");
			} 
			lockDir = new File(args[4] + "tmp/CP.SPLITER");			
			if (!lockDir.exists()) {
				throw new IllegalArgumentException("Not a valid locker!");
			}
		}
		if (newIns.newLock == 0 && newIns.stats == 0) {
			System.out.println("Putting file " + args[2] + "into existing locker " + args[4] + "...");
		}

		if (newIns.stats == 1) {
			if (args.length < 6) {
				throw new IllegalArgumentException("Please input file name you want to retrieve!");
			} else if (!(args[5].equals("all") || new File(args[4] + args[5]).exists())) {
				throw new IllegalArgumentException("File does not exist");
			}
		}
		
		newIns.inputfile = args[2];
		newIns.outputfile = args[4];
		
		Zip_Lib lib = new Zip_Lib(newIns.inputfile, newIns.outputfile);
		if (newIns.stats == 0) {
			lib.toCompact(newIns.newLock);
		} if (newIns.stats == 1) {
			lib.recover(args[5]);
		}
	}
}
