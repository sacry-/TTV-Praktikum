package battleshipz.battleshipz;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import de.uniba.wiai.lspi.chord.com.Node;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.PropertiesLoader;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

public class Main {

	public static void main(String[] args) throws IOException {
		
		BasicConfigurator.configure();
		
		PropertiesLoader.loadPropertyFile();

		Properties prop = Utils.loadLocalProperties("resources/battleshipz.properties");
		int iNumShips = Integer.parseInt(prop.getProperty("numShips").toString());
		int iNumFields = Integer.parseInt(prop.getProperty("numFields").toString());

		System.out.println("iNumShips: "+ iNumShips + " iNumFields: " + iNumFields);
		System.out.println(args);

		boolean fCreate = Boolean.parseBoolean(args[0]);
		String nodeURL = args[1];


		ChordImpl chord = new ChordImpl();
		ChordNotifyCallbackImpl callback = new ChordNotifyCallbackImpl(chord);
		chord.setCallback(callback);
		
		if (fCreate) {
			createCord(nodeURL, chord);
		} else {
			String strBootrapURL = args[2];
			joinCord(nodeURL, strBootrapURL, chord);
		}

		System.out.println("Hit enter to initialize game state");
		System.in.read();
		
		Game game = Game.createGame(chord, iNumShips, iNumFields);
		callback.setGame(game);
		System.out.println("My ID is: " + chord.getID().toHumanID());
		
		System.out.println("Hit enter to start the game");
		System.in.read();
		
		if(doIStart(chord)) {
			System.out.println("I am starting.");
			ID target = game.shootAtShip(new HashSet<Node>(chord.getFingerTable()));
			for(Node n : chord.getFingerTable()) {
				System.out.println("  " + n.getNodeID().toHumanID() + " " + n.getNodeURL());
			}
			System.out.println("shooting at target: " + target.toHumanID() );
			
			chord.retrieveAsync(target);
		}
	}
	
	private static boolean doIStart(ChordImpl chord) {
		return (chord.getPredecessorID() != null && ID.valueOf(Arithmetic.MAX_ID)
				.isInInterval(chord.getPredecessorID(), chord.getID())) || Arithmetic.MAX_ID
				.equals(chord.getID().toBigInteger());
	}
	
	private static void createCord(String strLocalURL, ChordImpl chord) {
		URL localURL = fetchURL(strLocalURL);
		try {
			System.out.println(localURL);
			chord.create(localURL);
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		}
	}

	private static void joinCord(String strNodeUrl, String strBootstrapURL, ChordImpl chord) {
		URL bootstrapURL = fetchURL(strBootstrapURL);
		URL nodeURL = fetchURL(strNodeUrl);
		try {
			System.out.println(bootstrapURL);
			chord.setURL(nodeURL);
			chord.join(bootstrapURL);
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		}
	}

	private static URL fetchURL(String strUrl) {
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
