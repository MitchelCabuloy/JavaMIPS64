package architecture;

import java.util.HashMap;
import java.util.Map.Entry;

public class Memory {
	private int[] memory_;
	private HashMap<Integer, Integer> transactions;

	public Memory() {
		// Declare size to be 2000 units
		// The first 1000 (0x0000 to 0x0FFF) is the code segment
		// Rest is main memory
		this.memory_ = new int[0x2000];
		this.transactions = new HashMap<Integer, Integer>();
	}

	public int getMemoryAddress(int address) {
		return this.memory_[address];
	}

	public void setMemoryAddress(int address, int value) {
		// this.memory_[address] = value;
		this.transactions.put(address, value);
	}

	public void cancelTransaction() {
		this.transactions.clear();
	}

	public void commit() {
		for (Entry<Integer, Integer> entry : this.transactions.entrySet()) {
			this.memory_[entry.getKey()] = entry.getValue();
		}

		this.transactions.clear();
	}

	// Debug code
	public void seeMemory() {
		int i = 0;
		for (Integer value : this.memory_) {
			if (value != 0)
				System.out.println(i + ": " + value);
			i++;
		}
	}
}
