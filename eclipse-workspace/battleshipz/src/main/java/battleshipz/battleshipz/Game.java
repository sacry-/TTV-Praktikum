package battleshipz.battleshipz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import de.uniba.wiai.lspi.chord.com.Node;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

public class Game {

	private int numberShips = 10;
	private int numberFields = 100;

	public Pirate me;
	private Map<ID, ID[]> playerSectors = new HashMap<ID, ID[]>();

	private Map<ID, Integer> playerHitMap = new HashMap<ID, Integer>();
	private Set<ID> fieldHitMap = new HashSet<ID>();
	

	public static Game createGame(ChordImpl chordImpl, int numberShips, int numberFields) {
		ID[] sectors = Arithmetic.calculateSectors(chordImpl.getPredecessorID(), chordImpl.getID(), numberFields);
		Pirate me = new Pirate(chordImpl.getID(),sectors ,numberShips, numberFields);
		return new Game(numberShips, numberFields, me, new HashSet<Node>(chordImpl.getFingerTable()));
	}
	

	private Game(int numberShips, int numberFields, Pirate me, Set<Node> fingertable) {
		this.numberShips = numberShips;
		this.numberFields = numberFields;
		this.me = me;
		
		List<ID> knownPlayerIDs = new ArrayList<ID>();
		knownPlayerIDs.add(me.id);
		for (Node node : fingertable) {
			knownPlayerIDs.add(node.getNodeID());
		}

		Collections.sort(knownPlayerIDs);

		for (int i = 0; i < knownPlayerIDs.size() - 1; i++) {
			ID player1 = knownPlayerIDs.get(i);
			ID player2 = knownPlayerIDs.get(i + 1);
			ID[] newSectors = Arithmetic.calculateSectors(player1, player2, numberFields);
			playerSectors.put(player1, newSectors);
		}
		ID[] newSectors = Arithmetic.calculateSectors(knownPlayerIDs.get(knownPlayerIDs.size() - 1),
				knownPlayerIDs.get(0), numberFields);
		playerSectors.put(knownPlayerIDs.get(knownPlayerIDs.size() - 1), newSectors);
	}
	

	public int amIShoot(ID target) {
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
			
			if(playerHit >= numberShips && source.compareTo(me.id) != 0 ) {
				System.out.println("Player " + source.toHumanID() + " is dead. I WOOOON!" + " " + new Date());
			}			
		}else {
			playerHitMap.put(source, 1);
		}

		System.out.println("---------------------------------------");
		for(ID id : playerHitMap.keySet()){
			System.out.println("   Player " +id.toHumanID() + " has " + (numberShips - playerHitMap.get(id)) + " left");
		}
		System.out.println("---------------------------------------");	
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
