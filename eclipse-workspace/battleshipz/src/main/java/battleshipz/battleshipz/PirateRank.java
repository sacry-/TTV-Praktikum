package battleshipz.battleshipz;

import java.util.Comparator;
import java.util.Random;

public class PirateRank implements Comparator<Pirate> {

	public int compare(Pirate pirate1, Pirate pirate2) {

		// maximum strategy; shoot at weakest player
		if (pirate1.getHits() > pirate2.getHits()) {
			return -1;
		} else if (pirate2.getHits() > pirate1.getHits()) {
			return 1;
		} else {
			if (new Random().nextBoolean()) {
				return -1;
			} else {
				return 1;
			}
		}
	}
}
