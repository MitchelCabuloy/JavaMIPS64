package models;

import java.util.HashMap;

import util.ByteUtils;
import architecture.Decoder;
import exceptions.InvalidSyntaxException;
import exceptions.RegisterOutOfBoundsException;

public class Code {
	private int address;
	private int instruction;
	private String codeString;

	public Code(String codeString, HashMap<Integer, Integer> labels, int lineNumber) throws InvalidSyntaxException, RegisterOutOfBoundsException {
		this.codeString = codeString;
		this.instruction = Decoder.decode(codeString, labels, lineNumber);
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

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}
}
