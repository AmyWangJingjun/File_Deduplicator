import java.io.IOException;

public class InstaZip {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Zip_Lib newIns = new Zip_Lib("/Users/jiahaozhao/Desktop/EC504/Project/test/");
		
		//toCompact and recover should not run at the same time.
		//newIns.toCompact();
		newIns.recover();
	}
}
