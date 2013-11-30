package architecture;

import java.util.HashMap;
import java.util.Map.Entry;

public class Memory {
	private byte[] memory_;
	private HashMap<Integer, Byte> transactions;

	public Memory() {
		// Declare size to be 2000 units
		// The first 1000 (0x0000 to 0x0FFF) is the code segment
		// Rest is main memory
		this.memory_ = new byte[0x2000];
		this.transactions = new HashMap<Integer, Byte>();
	}

	public int getMemoryAddress(int address) {
		return this.memory_[address];
	}

	public void setMemoryAddress(int address, byte value) {
		// this.memory_[address] = value;
		this.transactions.put(address, value);
	}

	public void cancelTransaction() {
		this.transactions.clear();
	}

	public void commit() {
		for (Entry<Integer, Byte> entry : this.transactions.entrySet()) {
			this.memory_[entry.getKey()] = entry.getValue();
		}

		this.transactions.clear();
	}

	// Debug code
	public void seeMemory() {
		int i = 0;
		for (Byte value : this.memory_) {
			if (value != 0)
				System.out.println(i + ": " + value);
			i++;
		}
	}
}
