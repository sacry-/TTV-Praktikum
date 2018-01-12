package battleshipz.battleshipz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.uniba.wiai.lspi.chord.data.ID;

public class Pirate {

	public ID id;
	public ID startField;
	public ID[] sectors;
	public List<Boolean> shipPlacement = new ArrayList<Boolean>();
	
	
	public Pirate(ID id, ID[] sectors) {
		this.id = id;
		this.sectors = sectors;
	}
	
	public boolean inIntervall(ID target) {
		for (int i = 0; i < sectors.length - 1; i++) {
			if (target.compareTo(sectors[i]) >= 0 && target.compareTo(sectors[i + 1]) < 0) {
				if (shipPlacement.get(i)) {
					shipPlacement.set(i, false);
					return true;
				} else {
					return false;
				}
			}
		}
		
		int lastIndex = sectors.length - 1;
	    if (target.compareTo(sectors[lastIndex]) >= 0 && target.compareTo(id) <= 0) {
	        if (shipPlacement.get(lastIndex)) {
	        	shipPlacement.set(lastIndex, false);
	        	return true;
	        }
	    }
	    
		return false;
	}
	
	public boolean shipsLeft() {
		return shipPlacement.contains(true);
	}

}
