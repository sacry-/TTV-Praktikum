package battleshipz.battleshipz;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {
	
	private static final boolean DEBUG=true;
	
	public static Properties loadLocalProperties(String path) {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(path);
			prop.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}
	
	public static void out(String msg) {
		if (DEBUG) {
			System.out.println(msg);
		}
	}
	
	public static void error(String msg) {
		System.out.println("--------- Error -----------");
		System.out.println(msg);
		System.out.println("---------------------------");
	}
}
