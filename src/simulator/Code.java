package simulator;

import util.ByteUtils;
import architecture.Decoder;

public class Code {
	private int instruction;
	private String codeString;

	public Code(String codeString) {
		this.codeString = codeString;
		this.instruction = Decoder.decode(codeString);
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public String getCodeString() {
		return codeString;
	}

	public void setCodeString(String codeString) {
		this.codeString = codeString;
	}

	public int getRS() {
		return ByteUtils.getRS(instruction);
	}

	public int getRD() {
		return ByteUtils.getRD(instruction);
	}

	public int getOpcode() {
		return ByteUtils.getOpcode(instruction);
	}

	public int getRT() {
		return ByteUtils.getRT(instruction);
	}

	public int getImmediate() {
		return ByteUtils.getImm(instruction);
	}

	public int getJOffset() {
		return ByteUtils.getJOffset(instruction);
	}

	public int getFunc() {
		return ByteUtils.getFunc(instruction);
	}

}
