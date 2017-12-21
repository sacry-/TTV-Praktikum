package battleshipz.battleshipz;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;


import org.junit.BeforeClass;
import org.junit.Test;

import de.uniba.wiai.lspi.chord.com.Node;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.NotifyCallback;
import de.uniba.wiai.lspi.chord.service.PropertiesLoader;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

public class BroadcastTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@Test
	public void test() {
PropertiesLoader.loadPropertyFile();
		

		ArrayList<ChordImpl> chordList = new ArrayList<ChordImpl>();
		final HashSet<ChordImpl> chordListForReceivedBroadcasts = new HashSet<ChordImpl>();
		String protocol = URL.KNOWN_PROTOCOLS.get(URL.LOCAL_PROTOCOL);

		URL localURL = null;

		try {
			localURL = new URL(protocol + "://localhost:8080/");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

		final ChordImpl chordLeader = new ChordImpl();

		NotifyCallback testNotifyCallbackLeader = new NotifyCallback() {
			
			public void retrieved(ID target) {
				chordLeader.broadcast(target, false);
				
			}
			
			public void broadcast(ID source, ID target, Boolean hit) {
				chordListForReceivedBroadcasts.add(chordLeader);
				
			}
		};

		chordLeader.setCallback(testNotifyCallbackLeader);
		try {
			chordLeader.create(localURL);
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
		chordList.add(chordLeader);
		System.out.println("New game created. Bootstrap node with ID: " + chordLeader.getID());
		
		for (int i = 10; i < 25; i++) {
			
			delay(200);
			
			final ChordImpl chordJoiner = new ChordImpl();
			NotifyCallback testNotifyCallbackJoiner = new NotifyCallback() {
				
				public void retrieved(ID target) {
					chordJoiner.broadcast(target, false);
					
				}
				
				public void broadcast(ID source, ID target, Boolean hit) {
					chordListForReceivedBroadcasts.add(chordJoiner);
					
				}
			};
			chordJoiner.setCallback(testNotifyCallbackJoiner);
			URL joinURL = null;
			try {
				joinURL = new URL(protocol + "://localhost:80" + i + "/");
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			try {
				chordJoiner.join(joinURL, localURL);
			} catch (ServiceException e1) {
				e1.printStackTrace();
			}
			chordList.add(chordJoiner);
			System.out.println("New node joined with ID: " + chordJoiner.getID());
			
	
		}

		Random r = new Random();
		BigInteger ranID = new BigInteger(160, r);
		delay(5000);
		
		for (ChordImpl tmpChord : chordList) {
			System.out.println("===============");
			System.out.println("Fingertable of: " + tmpChord.getID());
			for (Node finger : tmpChord.getFingerTable()) {
				System.out.println(finger.getNodeID());
			}
			System.out.println("===============");
		}
		chordLeader.broadcast(ID.valueOf(ranID), false);
		
		delay(5000);
		System.out.println("chordList " + chordList.size());
		for (ChordImpl chordImpl : chordList) {
			System.out.println(chordImpl);
		}
		System.out.println("chordListForReceivedBroadcasts " + chordListForReceivedBroadcasts.size());
		for (ChordImpl chordImpl : chordListForReceivedBroadcasts) {
			System.out.println(chordImpl);
		}
		assertTrue(new HashSet<ChordImpl>(chordList).equals(chordListForReceivedBroadcasts));
	}
	
	
	public static void delay(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
