package battleshipz.battleshipz;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

import de.uniba.wiai.lspi.chord.data.ID;

public class Arithmetic {

	public static final BigInteger MAX_ID = BigInteger.valueOf(2).pow(160).subtract(BigInteger.ONE);

	public static final ID LARGEST_ID = ID.valueOf(MAX_ID);

	private static ID add(ID id1, ID id2) {
		return ID.valueOf(id1.toBigInteger().add(id2.toBigInteger()));
	}

	private static ID add(ID id1, long value) {
		return ID.valueOf(id1.toBigInteger().add(BigInteger.valueOf(value)));
	}

	private static ID subtract(ID id1, ID value) {
		return ID.valueOf(id1.toBigInteger().subtract(value.toBigInteger()));
	}


	private static ID multiply(ID id1, long value) {
		return ID.valueOf(id1.toBigInteger().multiply(BigInteger.valueOf(value)));
	}

	private static ID divide(ID id1, long value) {

		return ID.valueOf(id1.toBigInteger().divide(BigInteger.valueOf(value)));
	}

	public static ID mod(ID id1, ID id2) {
		return ID.valueOf(id1.toBigInteger().mod(id2.toBigInteger()));
	}

	public static ID[] calculateSectors(ID from, ID to, int numberOfFields) {
		ID[] result = new ID[numberOfFields];
		ID distance;

		if (from.compareTo(to) < 0) {
			distance = subtract(to, from);
		} else {
			distance = add(subtract(LARGEST_ID, from), to);
		}

		ID step = divide(distance, numberOfFields);
		for (int i = 0; i < numberOfFields; i++) {
			ID id = add(from, 1);
			ID multiplied = multiply(step, i);
			id = add(id, multiplied);
			result[i] = mod(id, LARGEST_ID);
		}
		return result;
	}

	public static int isInSector(ID target, ID[] sector) {
		ID first = sector[0];
		ID last = sector[sector.length - 1];

		if (!target.isInInterval(first, last)) {
			return -1;
		}

		for (int i = 0; i < sector.length - 1; i++) {
			if (target.compareTo(sector[i]) >= 0 && target.compareTo(sector[i + 1]) < 0) {
				return i;
			}
		}
		System.out.println("is in sector");
		return -1;
	}

	public static ID selectShootID(List<ID> possibleTargets) {
		Random rnd = new Random();
		int randIndex = rnd.nextInt(possibleTargets.size());
		ID target = possibleTargets.get(randIndex);
		return mod(add(target, 10), LARGEST_ID);
	}

}
