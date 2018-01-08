package battleshipz.battleshipz;

import de.uniba.wiai.lspi.chord.data.ID;

public class Pirate implements Comparable<Pirate>{

	public ID id;
	public ID startField;
	private int numberShips;
	private int numberFields;
	
	
	
	public Pirate(ID id, int numberShips, int numberFields) {
		this.id = id;
		this.numberShips = numberShips;
		this.numberFields = numberFields;
	}
	
	
	
	public int getHits() {
		return -1;
	}



	public int compareTo(Pirate o) {
		return this.id.compareTo(o.id);
	}
	
	
	

}
