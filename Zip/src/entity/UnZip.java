package entity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.Enumeration;

public class UnZip {
	
	public static boolean decompressZip(File originFile, String targetDir) throws IOException {
     
        if (!targetDir.endsWith(File.separator)) {
            targetDir += File.separator;
        }
 
        ZipFile zipFile = new ZipFile(originFile);
        ZipEntry zipEntry;
        
        
        Enumeration<? extends ZipEntry> entry = zipFile.entries();
        while (entry.hasMoreElements()) {
            zipEntry = entry.nextElement();
            String fileName = zipEntry.getName();
            File outputFile = new File(targetDir + fileName);
            if (zipEntry.isDirectory()) {
            		(outputFile).mkdirs();
                continue;
            } else if (!outputFile.getParentFile().exists()) {
            		(new File(outputFile.getParent())).mkdirs();
            }
            OutputStream outputStream = new FileOutputStream(outputFile);
            InputStream inputStream = zipFile.getInputStream(zipEntry);
            int len;
            byte[] buffer = new byte[8192];
            while (-1 != (len = inputStream.read(buffer))) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.close();
            inputStream.close();
        }
        zipFile.close();
        return true;
    }
	
	public static void main(String[] args) throws Exception {	
		String originFile = "/Users/gejiali/study/ECE/Spring2018/EC504/project/test2.zip";
		String targetDir = "/Users/gejiali/study/ECE/Spring2018/EC504/project/unzip_test1";
		File file = new File(originFile);
		decompressZip(file, targetDir);
		
	}
	
}
