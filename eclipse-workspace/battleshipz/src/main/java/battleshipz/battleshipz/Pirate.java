package battleshipz.battleshipz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import de.uniba.wiai.lspi.chord.data.ID;

public class Pirate {
	private static Logger log = Logger.getLogger(Pirate.class);
	
	public ID id;
	public ID startField;
	public ID[] sectors;
	public List<Boolean> shipPlacement = new ArrayList<Boolean>();
	public ID lastShooter;
	
	
	public Pirate(ID id, ID[] sectors, int numShips, int numSectors) {
		this.id = id;
		this.sectors = sectors;
		for(int i=0; i< numSectors; i++) {
			shipPlacement.add(false);
		}
		for(int i=0; i< numShips; i++) {
			shipPlacement.set(i, true);
		}
		Collections.shuffle(shipPlacement);
	}
	
	public int inIntervall(ID target) {
		for (int i = 0; i < sectors.length - 1; i++) {
			if (target.compareTo(sectors[i]) >= 0 && target.compareTo(sectors[i + 1]) < 0) {
				if (shipPlacement.get(i)) {
					shipPlacement.set(i, false);
					return 1;
				} else {
					return 0;
				}
			}
		}
		
		int lastIndex = sectors.length - 1;
	    if (target.compareTo(sectors[lastIndex]) >= 0 && target.compareTo(id) <= 0) {
	        if (shipPlacement.get(lastIndex)) {
	        	shipPlacement.set(lastIndex, false);
	        	return 1;
	        }
	    }
	    
	    // Rarely happens
	    log.info("pirate is not inIntervall");
		return -1;
	}
	
	public boolean shipsLeft() {
		return shipPlacement.contains(true);
	}
	
	public void setlastShooter(ID lastShooter) {
		this.lastShooter = lastShooter;
	}
	
	public ID getlastShooter() {
		return this.lastShooter;
	}
}
