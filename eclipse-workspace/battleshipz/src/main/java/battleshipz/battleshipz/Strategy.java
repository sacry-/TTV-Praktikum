package battleshipz.battleshipz;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import de.uniba.wiai.lspi.chord.data.ID;

public class Strategy {
	
	private List<ID> possibleTargetSectors = null;
	private Map<ID, Integer> playerHitMap = null;
	private Set<ID> fieldHitMap;
	private Map<ID, ID[]> playerSectors;
	private int numberShips;
	private Pirate me;

	public Strategy(int numberShips, Pirate me, Map<ID, ID[]> playerSectors, List<ID> possibleTargetSectors, Map<ID, Integer> globalPlayerHitMap, Set<ID> fieldHitMap) {
		this.numberShips = numberShips;
		this.me = me;
		this.playerSectors = playerSectors;
		this.possibleTargetSectors = possibleTargetSectors;
		this.playerHitMap = createHitMap(globalPlayerHitMap, this.me);
		this.fieldHitMap = fieldHitMap;
	}
	
	public ID byStrategy(String strategy) {
		if (strategy == "shoot_back") {
			return selectShootBack();
		} else if (strategy == "minimum_ships") {
			return selectMinimumShips();
		} 
		return selectRandom();
	}
	
	// Return a random field that is not in my range and nobody shot at
	public ID selectRandom() {
		return selectField(possibleTargetSectors);
	}
	
	// Return a field from the player with most ships lost.
	public ID selectMinimumShips() {
		if (playerHitMap.size() <= 0) {
			return selectRandom();
		}
	
		ID minID  = playerHitMap.keySet().iterator().next();
		int min = numberShips - playerHitMap.get(minID);
		for(ID id : playerHitMap.keySet()){
			int cur = (numberShips - playerHitMap.get(id));
			if (cur < min) {
				minID = id;
				min = cur;
			}
		}
		return fetchSectorFromPlayer(minID);
	}

	// Return a field from the player that shot at us last
	public ID selectShootBack() {
		return fetchSectorFromPlayer(me.getlastShooter());
	}
	
	private ID fetchSectorFromPlayer(ID playerID) {
		if(playerID == null) {
			return selectRandom();
		}
		ID [] sectors = playerSectors.get(playerID);
		
		List<ID> targetSectors = Arrays.asList(sectors);
		for (ID id : fieldHitMap) {
			int index = Arithmetic.isInSector(id, sectors);
			if (index != -1) {
				targetSectors.remove(sectors[index]);
			}
		}
		
		if (targetSectors.size() > 0) {
			System.out.println("Custom selection...");
			return selectField(targetSectors);
		}
		
		return selectRandom();
	}
	
	private Map<ID, Integer> createHitMap(Map<ID, Integer> globalPlayerHitMap, Pirate me) {
		Map<ID, Integer> localPlayerHitMap= new HashMap<ID, Integer>();
		for (Map.Entry<ID, Integer> entry : globalPlayerHitMap.entrySet()) {
			if (entry.getKey().equals(me.id)) {
				continue;
			}
			localPlayerHitMap.put(entry.getKey(), entry.getValue());
		}
		return localPlayerHitMap;
	}
	
	private ID selectField(List<ID> possibleSectors) {
		Random rnd = new Random();
		int randIndex = rnd.nextInt(possibleSectors.size());
		ID targetField = possibleSectors.get(randIndex);
		return Arithmetic.mod(Arithmetic.add(targetField, 1), Arithmetic.LARGEST_ID);
	}
}
