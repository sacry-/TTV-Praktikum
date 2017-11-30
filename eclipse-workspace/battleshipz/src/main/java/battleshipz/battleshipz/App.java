package battleshipz.battleshipz;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.PropertiesLoader;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

public class App {
	private static Logger log = Logger.getLogger(App.class);

	private static Properties prop;

	public static void main(String[] args) {
		prop = loadLocalProperties();
		boolean fCreateNetwork = Boolean.valueOf(prop.get("createNetwork").toString());

		Chord chord;
		if (fCreateNetwork) {
			chord = createCord(prop.get("localUrl").toString());
		} else {
			chord = joinCord(prop.get("bootstrapUrl").toString());
		}
		

	}

	private static Properties loadLocalProperties() {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("resources/battleshipz.properties");
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

	private static Chord createCord(String strLocalURL) {
		URL localURL = fetchURL(strLocalURL);

		Chord chord = new ChordImpl();
		chord.setCallback(new ChordNotifyCallbackImpl());
		try {
			log.debug(localURL);
			chord.create(localURL);
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		}
		return chord;
	}

	private static Chord joinCord(String strBootstrapURL) {
		URL bootstrapURL = fetchURL(strBootstrapURL);

		Chord chord = new ChordImpl();
		chord.setCallback(new ChordNotifyCallbackImpl());
		try {
			log.debug(bootstrapURL);
			chord.join(bootstrapURL);
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		}
		return chord;
	}

	private static URL fetchURL(String strUrl) {
		PropertiesLoader.loadPropertyFile();
		String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);
		URL url = null;

		try {
			url = new URL(protocol + "://" + strUrl + "/");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		return url;
	}

}
