package battleshipz.battleshipz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniba.wiai.lspi.chord.com.Node;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

public class Game {

	private int numberShips = 0; 
	private int numberFields = 0;
	
	private Map<ID, Pirate> hmPirates;
	private Pirate me;
	
	
	public static Game createGame(ChordImpl chordImpl, int numberShips, int numberFields) {
		
		Pirate me = new Pirate(chordImpl.getID(), numberShips, numberFields);
		
		Game game = new Game(numberShips, numberFields, me);
		game.addUnknownPirates(chordImpl.getFingerTable());
		
		return game;
	}
	
	private Game(int numberShips, int numberFields, Pirate me) {
		this.numberShips = numberShips; 
		this.numberFields = numberFields; 
		this.me = me;
	}
	
	public void addUnknownPirates(List<Node> fingerTable) {
		for (Node n : fingerTable) {
			if (hmPirates.containsKey(n.getNodeID())) {
				Pirate pToAdd = new Pirate(n.getNodeID(), numberShips, numberFields);
				hmPirates.put(n.getNodeID(), pToAdd);
			}
		}
	}
	
	public void addNode(Node n) {
		if (!hmPirates.containsKey(n.getNodeID())) {
			this.hmPirates.put(n.getNodeID(), new Pirate(n.getNodeID(), this.numberFields, this.numberShips));
		}
	}
	
	public List<Pirate> getPirates(){
		
		return new ArrayList<Pirate>(hmPirates.values());		
	}
	
	public Pirate getMe() {
		return me;
	}
	

}
