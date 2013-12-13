package architecture;

import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.table.AbstractTableModel;

public class Registers {
	private long[] registers;
	private HashMap<String, Long> specialRegisters;
	private HashMap<Object, Long> transactions;

	private RegistersTableModel registersTableModel;
	private SpecialRegistersTableModel specialRegistersTableModel;

	public Registers() {
		this.registers = new long[0x20];
		this.specialRegisters = new HashMap<String, Long>();

		this.registersTableModel = new RegistersTableModel(registers);
		this.specialRegistersTableModel = new SpecialRegistersTableModel(
				specialRegisters);

		this.specialRegisters.put("PC", 0L);

		this.specialRegisters.put("IF/ID.IR", 0L);
		// this.specialRegisters.put("IF/ID.NPC", 0L);

		this.specialRegisters.put("ID/EX.A", 0L);
		this.specialRegisters.put("ID/EX.B", 0L);
		this.specialRegisters.put("ID/EX.IMM", 0L);
		this.specialRegisters.put("ID/EX.IR", 0L);
		// this.specialRegisters.put("ID/EX.NPC", 0L);

		this.specialRegisters.put("EX/MEM.ALUOUTPUT", 0L);
		// this.specialRegisters.put("EX/MEM.Cond", 0L);
		this.specialRegisters.put("EX/MEM.IR", 0L);
		this.specialRegisters.put("EX/MEM.B", 0L);

		this.specialRegisters.put("MEM/WB.ALUOUTPUT", 0L);
		this.specialRegisters.put("MEM/WB.LMD", 0L);
		this.specialRegisters.put("MEM/WB.IR", 0L);

		this.transactions = new HashMap<Object, Long>();
	}

	// For numbered Registers (R0, R1, R2)
	public long getRegister(int number) {
		if (number == 0)
			// For R0
			return 0;
		else if (number > 0 && number < 32)
			return this.registers[number];
		else
			throw new RegisterOutOfBoundsException(number);
	}

	public void setRegister(int number, long value) {
		if (number == 0) {
			// Silently fail if someone is trying to set R0
		} else if (number > 0 && number < 32)
			// this.registers[number] = value;
			this.transactions.put(number, value);
		else
			throw new RegisterOutOfBoundsException(number);
	}

	// For Special Registers
	public long getRegister(String register) {
		register = register.toUpperCase();
		if (this.specialRegisters.containsKey(register))
			return this.specialRegisters.get(register);
		else
			throw new RegisterOutOfBoundsException(register);
	}

	public void setRegister(String register, long value) {
		register = register.toUpperCase();
		if (this.specialRegisters.containsKey(register))
			this.transactions.put(register, value);
		else
			throw new RegisterOutOfBoundsException(register);
	}

	public RegistersTableModel getRegistersTableModel() {
		return registersTableModel;
	}

	public void setRegistersTableModel(RegistersTableModel registersTableModel) {
		this.registersTableModel = registersTableModel;
	}

	public SpecialRegistersTableModel getSpecialRegistersTableModel() {
		return specialRegistersTableModel;
	}

	public void setSpecialRegistersTableModel(
			SpecialRegistersTableModel specialRegistersTableModel) {
		this.specialRegistersTableModel = specialRegistersTableModel;
	}

	public void cancelTransaction() {
		this.transactions.clear();
	}

	public void commit() {
		for (Entry entry : this.transactions.entrySet()) {
			if (entry.getKey() instanceof Integer) {
				this.registers[(Integer) entry.getKey()] = (Long) entry
						.getValue();
			} else if (entry.getKey() instanceof String) {
				this.specialRegisters.put((String) entry.getKey(),
						(Long) entry.getValue());
			}
		}

		this.transactions.clear();
	}

	// Debug code
	public void seeRegisters() {
		int i = 0;
		for (Long value : this.registers) {
			if (value != 0)
				System.out.println(String.format("R%02d: %016x", i, value));
			i++;
		}
		for (Entry<String, Long> entry : this.specialRegisters.entrySet()) {
			System.out.println(String.format("%s: %016x", entry.getKey(),
					entry.getValue()));
		}
	}
}

class RegistersTableModel extends AbstractTableModel {
	private long[] registers;

	public RegistersTableModel(long[] registers) {
		this.registers = registers;
	}

	@Override
	public String getColumnName(int column) {
		if (column == 0)
			return "Register";
		else
			return "Value";
	}

	@Override
	public int getRowCount() {
		return 32;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0)
			return "R" + rowIndex;
		else
			return String.format("%016x", registers[rowIndex]).toUpperCase();
	}

}

class SpecialRegistersTableModel extends AbstractTableModel {
	HashMap<String, Long> registers;

	public SpecialRegistersTableModel(HashMap<String, Long> registers) {
		this.registers = registers;
	}

	@Override
	public String getColumnName(int column) {
		if (column == 0)
			return "Register";
		else
			return "Value";
	}

	@Override
	public int getRowCount() {
		return 12;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			switch (rowIndex) {
			case 0:
				return "PC";
			case 1:
				return "IF/ID.IR";
			case 2:
				return "ID/EX.A";
			case 3:
				return "ID/EX.B";
			case 4:
				return "ID/EX.Imm";
			case 5:
				return "ID/EX.IR";
			case 6:
				return "EX/MEM.ALUOutput";
			case 7:
				return "EX/MEM.IR";
			case 8:
				return "EX/MEM.B";
			case 9:
				return "MEM/WB.ALUOutput";
			case 10:
				return "MEM/WB.LMD";
			case 11:
				return "MEM/WB.IR";
			}
		} else {
			switch (rowIndex) {
			case 0:
				return String.format("%016x", registers.get("PC"))
						.toUpperCase();
			case 1:
				return String.format("%016x", registers.get("IF/ID.IR"))
						.toUpperCase();
			case 2:
				return String.format("%016x", registers.get("ID/EX.A"))
						.toUpperCase();
			case 3:
				return String.format("%016x", registers.get("ID/EX.B"))
						.toUpperCase();
			case 4:
				return String.format("%016x", registers.get("ID/EX.IMM"))
						.toUpperCase();
			case 5:
				return String.format("%016x", registers.get("ID/EX.IR"))
						.toUpperCase();
			case 6:
				return String
						.format("%016x", registers.get("EX/MEM.ALUOUTPUT"))
						.toUpperCase();
			case 7:
				return String.format("%016x", registers.get("EX/MEM.IR"))
						.toUpperCase();
			case 8:
				return String.format("%016x", registers.get("EX/MEM.B"))
						.toUpperCase();
			case 9:
				return String
						.format("%016x", registers.get("MEM/WB.ALUOUTPUT"))
						.toUpperCase();
			case 10:
				return String.format("%016x", registers.get("MEM/WB.LMD"))
						.toUpperCase();
			case 11:
				return String.format("%016x", registers.get("MEM/WB.IR"))
						.toUpperCase();
			}
		}

		return null;
	}
}

class RegisterOutOfBoundsException extends RuntimeException {
	public RegisterOutOfBoundsException() {
	}

	public RegisterOutOfBoundsException(String register) {
		super(String.format("Register %s is not a valid register", register));
	}

	public RegisterOutOfBoundsException(int number) {
		super(String.format("Register R%d is not a valid register", number));
	}
}
