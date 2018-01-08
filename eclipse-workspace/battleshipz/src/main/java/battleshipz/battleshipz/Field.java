package battleshipz.battleshipz;

import java.math.BigInteger;

public class Field {
	public enum FieldStateEnum{
		HIT, MISS, TARGET;
	}
	
	private int fieldNumber;
	private BigInteger startHash = null;
	private BigInteger endHash = null;
	private FieldStateEnum fieldState;
	

	public Field(int fieldNumber, BigInteger startHash, BigInteger endHash, FieldStateEnum state) {
		this.fieldNumber = fieldNumber;
		this.startHash = startHash;
		this.endHash = endHash;
		this.fieldState = state;
	}
	
	public boolean isInside(BigInteger field) {
		if (startHash.compareTo(endHash) < 0) {
			return field.compareTo(startHash) > -1 && field.compareTo(endHash) < 1;
		} else {
			return field.compareTo(startHash) > -1 || field.compareTo(endHash) < 1;
		}
	}

	public int getFieldNumber() {
		return fieldNumber;
	}

	public void setFieldNumber(int fieldNumber) {
		this.fieldNumber = fieldNumber;
	}

	public BigInteger getStartHash() {
		return startHash;
	}

	public void setStartHash(BigInteger startHash) {
		this.startHash = startHash;
	}

	public BigInteger getEndHash() {
		return endHash;
	}

	public void setEndHash(BigInteger endHash) {
		this.endHash = endHash;
	}

	public FieldStateEnum getFieldState() {
		return fieldState;
	}

	public void setFieldState(FieldStateEnum fieldState) {
		this.fieldState = fieldState;
	}

}

