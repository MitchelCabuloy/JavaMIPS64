package architecture;

import java.util.HashMap;
import java.util.Map.Entry;

public class Memory {
	private byte[] memory;
	private HashMap<Integer, Byte> transactions;

	public Memory() {
		// Declare size to be 2000 units
		// The first 1000 (0x0000 to 0x0FFF) is the code segment
		// Rest is main memory
		this.memory = new byte[0x2000];
		this.transactions = new HashMap<Integer, Byte>();
	}

	public int getMemoryAddress(int address) {
		if (address >= 0x1000 && address < 0x2000) {
			return this.memory[address];
		} else {
			throw new MemoryOutOfRangeException(address);
		}
	}

	public void setMemoryAddress(int address, byte value) {
		if (address >= 0x1000 && address < 0x2000) {
			this.transactions.put(address, value);
		} else {
			throw new MemoryOutOfRangeException(address);
		}
	}
	
	public int getCodeSegment(int lineNumber){
		// If line number is within 0 && 0x400 lines (because of 0 to 0x1000 code segment)
		if(lineNumber >= 0 && lineNumber < 0x400){
			return this.memory[lineNumber * 4];
		} else {
			throw new CodeSegmentOutOfRangeException(lineNumber);
		}
	}
	
	public void setCodeSegment(int lineNumber, byte[] code){
		// If line number is within 0 && 0x400 lines (because of 0 to 0x1000 code segment)
		if(lineNumber >= 0 && lineNumber < 0x400){
			int address = lineNumber * 4;
			for(int i = 0; i < 4; i++){
				this.memory[address + i] = code[i];
			}
		} else {
			throw new CodeSegmentOutOfRangeException(lineNumber);
		}
	}

	public void cancelTransaction() {
		this.transactions.clear();
	}

	public void commit() {
		for (Entry<Integer, Byte> entry : this.transactions.entrySet()) {
			this.memory[entry.getKey()] = entry.getValue();
		}

		this.transactions.clear();
	}

	// Debug code
	public void seeMemory() {
		int i = 0;
		for (Byte value : this.memory) {
			if (value != 0)
				System.out.println(i + ": " + value);
			i++;
		}
	}
}

class MemoryOutOfRangeException extends RuntimeException {
	public MemoryOutOfRangeException() {
		super("Memory address entered is beyond the 0x1000 to 0x1FFF range");
	};

	public MemoryOutOfRangeException(int address) {
		super(String.format(
				"Memory address %04x is beyond the 0x1000 to 0x1FFF range",
				address));
	}
}

class CodeSegmentOutOfRangeException extends RuntimeException{
	public CodeSegmentOutOfRangeException(){
		super("Code segment entered is out of range");
	}
	
	public CodeSegmentOutOfRangeException(int lineNumber){
		super("Code segment " + lineNumber + " is out of range");
	}
}