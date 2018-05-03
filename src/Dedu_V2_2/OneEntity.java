package Dedu_V2_2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class OneEntity {
	
	private static final int  BUFFER_SIZE = 2 * 1024;
	
	public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure)
			throws RuntimeException{
		
		ZipOutputStream zos = null ;
		try {
			zos = new ZipOutputStream(out);
			File sourceFile = new File(srcDir);
			compress(sourceFile,zos,sourceFile.getName(),KeepDirStructure);
		} catch (Exception e) {
			throw new RuntimeException("zip error from OneEntity",e);
		}finally{
			if(zos != null){
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	private static void compress(File sourceFile, ZipOutputStream zos, String name,
			boolean KeepDirStructure) throws Exception{
		byte[] buf = new byte[BUFFER_SIZE];
		if(sourceFile.isFile()){
			zos.putNextEntry(new ZipEntry(name));
			int len;
			FileInputStream in = new FileInputStream(sourceFile);
			while ((len = in.read(buf)) != -1){
				zos.write(buf, 0, len);
			}
			// Complete the entry
			zos.closeEntry();
			in.close();
		} else {
			File[] listFiles = sourceFile.listFiles();
			if(listFiles == null || listFiles.length == 0){
				if(KeepDirStructure){
					zos.putNextEntry(new ZipEntry(name + "/"));
					zos.closeEntry();
				}
				
			}else {
				for (File file : listFiles) {
					if (KeepDirStructure) {
						compress(file, zos, name + "/" + file.getName(),KeepDirStructure);
					} else {
						compress(file, zos, file.getName(),KeepDirStructure);
					}
					
				}
			}
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		if (args[0].charAt(args[0].length() - 1) == '/') {
			args[0] = args[0].substring(0, args[0].length() - 1);
		}
		FileOutputStream fos1 = new FileOutputStream(new File(args[0] + ".zip"));
		OneEntity.toZip(args[0], fos1, true);

	}
}
