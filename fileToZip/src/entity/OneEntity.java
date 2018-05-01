package entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class OneEntity {
	
	private static final int  BUFFER_SIZE = 2 * 1024;
	
	public static void toZip(List<File> srcFiles , OutputStream out)throws RuntimeException {
		long start = System.currentTimeMillis();
		ZipOutputStream zos = null ;
		try {
			zos = new ZipOutputStream(out);
			for (File srcFile : srcFiles) {
				byte[] buf = new byte[BUFFER_SIZE];
				zos.putNextEntry(new ZipEntry(srcFile.getName()));
				int len;
				FileInputStream in = new FileInputStream(srcFile);
				while ((len = in.read(buf)) != -1){
					zos.write(buf, 0, len);
				}
				zos.closeEntry();
				in.close();
			}
			long end = System.currentTimeMillis();
			System.out.println("the time for compression is：" + (end - start) +" ms");
		} catch (Exception e) {
			throw new RuntimeException("zip error from ZipUtils",e);
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
	
	
	public static void main(String[] args) throws Exception {
		List<File> fileList = new ArrayList<>();
		fileList.add(new File("/Users/gejiali/study/ECE/Spring2018/EC504/project/1.pdf"));
		fileList.add(new File("/Users/gejiali/study/ECE/Spring2018/EC504/project/2.pdf"));
		FileOutputStream fos2 = new FileOutputStream(new File("/Users/gejiali/study/ECE/Spring2018/EC504/project/test1.zip"));
		OneEntity.toZip(fileList, fos2);
	}
}
