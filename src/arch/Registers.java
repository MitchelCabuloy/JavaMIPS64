package arch;

import java.util.HashMap;

public class Registers {
	private long programCounter;
	private long[] registers;
	private HashMap<String, Long> specialRegisters;

	public Registers() {
		this.registers = new long[0x20];
		this.programCounter = 0;
		this.specialRegisters = new HashMap<String, Long>();

		this.specialRegisters.put("IF/ID.IR", new Long(0));
		this.specialRegisters.put("IF/ID.NPC", new Long(0));

		this.specialRegisters.put("ID/EX.A", new Long(0));
		this.specialRegisters.put("ID/EX.B", new Long(0));
		this.specialRegisters.put("ID/EX.Imm", new Long(0));
		this.specialRegisters.put("ID/EX.IR", new Long(0));
		this.specialRegisters.put("ID/EX.NPC", new Long(0));

		this.specialRegisters.put("EX/MEM.ALUOutput", new Long(0));
		this.specialRegisters.put("EX/MEM.Cond", new Long(0));
		this.specialRegisters.put("EX/MEM.IR", new Long(0));
		this.specialRegisters.put("EX/MEM.B", new Long(0));

		this.specialRegisters.put("MEM/WB.ALUOutput", new Long(0));
		this.specialRegisters.put("MEM/WB.LMD", new Long(0));
		this.specialRegisters.put("MEM/WB.IR", new Long(0));
	}

	// For numbered Registers (R0, R1, R2)
	public long getRegister(int number) throws RegisterOutOfBoundsException {
		if (number == 0)
			// For R0
			return 0;
		else if (number > 0 && number < 32)
			return this.registers[number];
		else
			throw new RegisterOutOfBoundsException(number);
	}

	public void setRegister(int number, long value) throws RegisterOutOfBoundsException{
		if (number > 0 && number < 32)
			this.registers[number] = value;
		else
			throw new RegisterOutOfBoundsException(number);
	}

	// For Special Registers
	public long getRegister(String register) throws RegisterOutOfBoundsException{
		if (this.specialRegisters.containsKey(register))
			return this.specialRegisters.get(register);
		else
			throw new RegisterOutOfBoundsException(register);
	}

	public void setRegister(String register, long value)
			throws RegisterOutOfBoundsException {
		if (this.specialRegisters.containsKey(register))
			this.specialRegisters.put(register, value);
		else
			throw new RegisterOutOfBoundsException(register);
	}
}

class RegisterOutOfBoundsException extends Exception {
	public RegisterOutOfBoundsException() {
	}

	public RegisterOutOfBoundsException(String register) {
		super(String.format("Register %s is not a valid register", register));
	}

	public RegisterOutOfBoundsException(int number) {
		super(String.format("Register R%d is not a valid register", number));
	}
}
