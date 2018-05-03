package entity;

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
		FileOutputStream fos1 = new FileOutputStream(new File("/Users/gejiali/study/ECE/Spring2018/EC504/project/test2.zip"));
		OneEntity.toZip("/Users/gejiali/study/ECE/Spring2018/EC504/project/testing", fos1, true);

	}
}
