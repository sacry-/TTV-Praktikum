package battleshipz.battleshipz;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
	
	public static ID selectFieldToShootAt(Pirate pirate) {
		ID result = null;

		Random random = new Random();

		if (pirate != null) { // if a player was found
			List<Field> playerFields = pirate.getFields();
			List<Field> unusedFields = playerFields.stream()
					.filter(field -> field.getState().equals(FieldStatus.NOT_TOUCHED)).collect(Collectors.toList());
			if (!unusedFields.isEmpty()) {
				Field field = unusedFields.get(random.nextInt(unusedFields.size()));
				result = ID.valueOf(calcMiddle(field.getStartHash(), field.getEndHash()));
			}
		}

		if (pirate == null
				|| result == null) { // if no player was found to shoot at, take a random field which is not MINE
			BigInteger selfStart = Players.me.getStartField().toBigInteger();
			BigInteger selfEnd = Players.me.getId().toBigInteger();
			BigInteger fieldNrToShootAt = BigInteger.ZERO;
			while (fieldNrToShootAt.compareTo(selfStart) < 0 && fieldNrToShootAt.compareTo(selfEnd) > 0) {
				fieldNrToShootAt = new BigInteger(Main.NR_BITS_ID, random);
			}
			result = ID.valueOf(fieldNrToShootAt);
		}

		return result;
	}
	
	
}
