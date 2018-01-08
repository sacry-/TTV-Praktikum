package battleshipz.battleshipz;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import de.uniba.wiai.lspi.chord.data.ID;

public class ShootingLogic {
	private static Logger log = Logger.getLogger(ShootingLogic.class);

	
	private Game game;
	
	
	public ShootingLogic(Game game) {
		this.game = game;
	}
	
	
	public void initShot() {
		
	}
	
	
	public Pirate firstShootAtPirate() {
		
		return null;
	}
	
	public Pirate selectPirateToShootAt() {
		
		List<Pirate> pirates = game.getPirates();
		Collections.sort(pirates, new PirateRank());
		
		for (Pirate player : pirates) {
			if (!player.equals(game.getMe())) { // get first player, which is not ME
				return player;
			}
		}
		
		log.error("Oh no, there are no pirates to shoot at.");
		return null;
		
	}
	
	
}
