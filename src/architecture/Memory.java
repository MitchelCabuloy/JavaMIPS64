package architecture;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.table.AbstractTableModel;

public class Memory {
	private HashMap<Integer, Byte> memory;
	private HashMap<Integer, Byte> transactions;
	private MemoryTableModel memoryTableModel;

	public Memory() {
		// Declare size to be 2000 units
		// The first 1000 (0x0000 to 0x0FFF) is the code segment
		// Rest is main memory
		memory = new HashMap<Integer, Byte>();
		transactions = new HashMap<Integer, Byte>();
		memoryTableModel = new MemoryTableModel(memory);
	}

	public byte getMemoryAddress(int address) {
		if (address >= 0x1000 && address < 0x2000) {
			if (memory.containsKey(address))
				return memory.get(address);
			else
				return 0;
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

	public int getCodeSegment(int lineNumber) {
		// If line number is within 0 && 0x400 lines (because of 0 to 0x1000
		// code segment)
		if (lineNumber >= 0 && lineNumber < 0x400) {
			int address = lineNumber * 4;

			ByteBuffer buffer = ByteBuffer.allocate(4);
			buffer.order(ByteOrder.BIG_ENDIAN);

			byte b = 0;
			for (int i = 0; i < 4; i++) {
				if (memory.containsKey(address + i))
					buffer.put(memory.get(address + i));
				else
					buffer.put(b);
			}

			buffer.rewind();
			return buffer.getInt();
		} else {
			throw new CodeSegmentOutOfRangeException(lineNumber);
		}
	}

	public void setCodeSegment(int lineNumber, int code) {
		// If line number is within 0 && 0x400 lines (because of 0 to 0x1000
		// code segment)
		if (lineNumber >= 0 && lineNumber < 0x400) {
			ByteBuffer buffer = ByteBuffer.allocate(4);
			buffer.order(ByteOrder.BIG_ENDIAN);
			buffer.putInt(code);

			byte[] codeSegment = buffer.array();
			int address = lineNumber * 4;
			for (int i = 0; i < 4; i++) {
				this.memory.put(address + i, codeSegment[i]);
			}
		} else {
			throw new CodeSegmentOutOfRangeException(lineNumber);
		}
	}

	public MemoryTableModel getMemoryTableModel() {
		return memoryTableModel;
	}

	public void setMemoryTableModel(MemoryTableModel memoryTableModel) {
		this.memoryTableModel = memoryTableModel;
	}

	public void cancelTransaction() {
		this.transactions.clear();
	}

	public void commit() {
		for (Entry<Integer, Byte> entry : this.transactions.entrySet()) {
			memory.put(entry.getKey(), entry.getValue());
		}

		this.transactions.clear();
	}

	public static long getLMD(long IR, Registers registers, Memory memory) {
		int address = (int) registers.getRegister("EX/MEM.ALUOUTPUT");
		String byteString = "";
		for (int i = 0; i < 4; i++) {
			byteString += byteString
					+ String.format(
							"%8s",
							Integer.toBinaryString(memory
									.getMemoryAddress(address + i) & 0xFF))
							.replace(' ', '0');
		}

		return Long.parseLong(byteString, 2);
	}

	// Debug code
	public void seeMemory() {
		int i = 0;
		// for (Byte value : this.memory) {
		// if (value != 0)
		// System.out.println(i + ": " + value);
		// i++;
		// }
		for (Entry<Integer, Byte> entry : memory.entrySet()) {
			if (entry.getValue() != 0)
				System.out.println(String.format("%04x: %02x", entry.getKey(),
						entry.getValue()));
		}
	}
}

class MemoryTableModel extends AbstractTableModel {
	private HashMap<Integer, Byte> memory;

	public MemoryTableModel(HashMap<Integer, Byte> memory) {
		this.memory = memory;
	}
	
	@Override
	public String getColumnName(int column) {
		if (column == 0)
			return "Address";
		else
			return "Value";
	}

	@Override
	public int getRowCount() {
		return memory.size();
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		int i = 0;
		for (Entry<Integer, Byte> entry : memory.entrySet()) {
			if (i == rowIndex) {
				if (columnIndex == 0) {
					return entry.getKey();
				} else {
					return String.format("%02x",entry.getValue());
				}
			}
			i++;
		}
		return null;
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

class CodeSegmentOutOfRangeException extends RuntimeException {
	public CodeSegmentOutOfRangeException() {
		super("Code segment entered is out of range");
	}

	public CodeSegmentOutOfRangeException(int lineNumber) {
		super("Code segment " + lineNumber + " is out of range");
	}
}