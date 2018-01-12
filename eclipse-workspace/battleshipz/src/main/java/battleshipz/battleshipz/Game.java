package battleshipz.battleshipz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import de.uniba.wiai.lspi.chord.com.Node;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

public class Game {
	private static Logger log = Logger.getLogger(Game.class);

	private int numberShips = 10;
	private int numberFields = 100;

	public Pirate me;
	private Map<ID, ID[]> playerSectors = new HashMap<ID, ID[]>();

	private Map<ID, Integer> playerHitMap = new HashMap<ID, Integer>();
	private Set<ID> fieldHitMap = new HashSet<ID>();
	

	public static Game createGame(ChordImpl chordImpl, int numberShips, int numberFields) {

		Pirate me = new Pirate(chordImpl.getID(), Arithmetic.calculateSectors(chordImpl.getID(), chordImpl.getPredecessorID(),numberFields));

		Game game = new Game(numberShips, numberFields, me, new HashSet<Node>(chordImpl.getFingerTable()));

		return game;
	}
	

	private Game(int numberShips, int numberFields, Pirate me, Set<Node> fingertable) {
		this.numberShips = numberShips;
		this.numberFields = numberFields;
		this.me = me;

		// init playersectors
		List<ID> knownPlayerIDs = new ArrayList<ID>();

		knownPlayerIDs.add(me.id);

		for (Node node : fingertable) {
			knownPlayerIDs.add(node.getNodeID());
		}

		Collections.sort(knownPlayerIDs);

		// calculate sectors
		for (int i = 0; i < knownPlayerIDs.size() - 1; i++) {
			ID player1 = knownPlayerIDs.get(i);
			ID player2 = knownPlayerIDs.get(i + 1);
			ID[] newSectors = Arithmetic.calculateSectors(player1, player2, numberFields);
			playerSectors.put(player1, newSectors);
		}
		// case for the last player
		ID[] newSectors = Arithmetic.calculateSectors(knownPlayerIDs.get(knownPlayerIDs.size() - 1),
				knownPlayerIDs.get(0), numberFields);
		playerSectors.put(knownPlayerIDs.get(knownPlayerIDs.size() - 1), newSectors);
	}
	

	public boolean amIShoot(ID target) {
		return me.inIntervall(target);
	}
	
	public boolean shipsLeft() {
		return me.shipsLeft();
	}
	
	public void broadcast(ID source, ID target, Boolean hit) {
		fieldHitMap.add(source);
		if(!hit) {
			return;
		}
		
		if(playerHitMap.containsKey(source)) {
			int playerHit = playerHitMap.get(source) +1;
			playerHitMap.put(source, playerHit);
			
			if(playerHit >= numberShips) {
				log.info("Player " + source.toHumanID() + " is gone");
			}			
		}else {
			playerHitMap.put(source, 1);
		}

		log.info("---------------------------------------");
		for(ID id : playerHitMap.keySet()){
			log.info("   Player " +id.toHumanID() + " has " + (numberShips - playerHitMap.get(id)) + " left");
		}
		log.info("---------------------------------------");
		
	}

	
	
	public ID shootAtShip(Set<Node> fingertable) {
		List<ID> knownPlayerIDs = new ArrayList<ID>();

		List<ID> sectorsToShoot = new ArrayList<ID>();
		knownPlayerIDs.add(me.id);

		for (Node node : fingertable) {
			knownPlayerIDs.add(node.getNodeID());
		}

		Collections.sort(knownPlayerIDs);

		for (ID uniquePlayer : knownPlayerIDs) {
			for (int j = 0; j < numberFields; j++) {
				sectorsToShoot.add(playerSectors.get(uniquePlayer)[j]);
			}
		}

		// remove fields, that are already shot on
		for (ID id : fieldHitMap) {
			for (ID playerId : knownPlayerIDs) {
				ID[] sectors = playerSectors.get(playerId);
				int index = Arithmetic.isInSector(id, sectors);
				if (index != -1) {
					sectorsToShoot.remove(sectors[index]);
				}

			}
		}

		for (ID id : me.sectors) {
			sectorsToShoot.remove(id);
		}
		
		return Arithmetic.selectShootID(sectorsToShoot);
	}

}
