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
	
	public FieldStateEnum getFieldState() {
		return fieldState;
	}

	public void setFieldState(FieldStateEnum fieldState) {
		this.fieldState = fieldState;
	}
	
	
	
	
	

}

