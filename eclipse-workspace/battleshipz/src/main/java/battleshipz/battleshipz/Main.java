package battleshipz.battleshipz;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.PropertiesLoader;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

public class Main {
	private static Logger log = Logger.getLogger(Main.class);

	public static int NR_BITS_ID = 160;
	public static BigInteger MAX_ID = BigInteger.valueOf(2).pow(NR_BITS_ID).subtract(BigInteger.ONE);

	public static void main(String[] args) throws IOException {

		Properties prop = Utils.loadLocalProperties("resources/battleshipz.properties");
		int iNumShips = Integer.getInteger(prop.getProperty("numShips"));
		int iNumFields = Integer.getInteger(prop.getProperty("numFields"));
		
		System.out.println(args);

		boolean fCreate = Boolean.getBoolean(args[0]);
		String strURL = args[1];

		ChordImpl chord = new ChordImpl();
		Game game = Game.createGame(chord, iNumShips, iNumFields);
		ShootingLogic shootingLogic = new ShootingLogic(game);
		chord.setCallback(new ChordNotifyCallbackImpl(chord,game, shootingLogic));
		
		if (fCreate) {
			createCord(strURL, chord);
		} else {
			joinCord(strURL, chord);
		}

		
		
		if(doIStart(chord)) {
			System.out.println("Press button to start");
			System.in.read();
			
			game.shoot(shootingLogic.firstShootAtPlayer());

			//// first shot
			
			
		}

	}

	
	private static boolean doIStart(ChordImpl chord) {
		return (chord.getPredecessorID() != null && ID.valueOf(MAX_ID)
				.isInInterval(chord.getPredecessorID(), chord.getID())) || MAX_ID
				.equals(chord.getID().toBigInteger());
	}
	
	

	private static void createCord(String strLocalURL, ChordImpl chord) {
		URL localURL = fetchURL(strLocalURL);
		try {
			log.debug(localURL);
			chord.create(localURL);
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		}
	}

	private static void joinCord(String strBootstrapURL, ChordImpl chord) {
		URL bootstrapURL = fetchURL(strBootstrapURL);
		try {
			log.debug(bootstrapURL);
			chord.join(bootstrapURL);
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		}
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
