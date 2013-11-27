package arch;

import java.util.HashMap;

public class Registers {
	private long[] registers;
	private HashMap<String, Long> specialRegisters;

	public Registers() {
		this.registers = new long[0x20];
		this.specialRegisters = new HashMap<String, Long>();
		
		this.specialRegisters.put("PC", 0L);

		this.specialRegisters.put("IF/ID.IR", 0L);
		this.specialRegisters.put("IF/ID.NPC", 0L);

		this.specialRegisters.put("ID/EX.A", 0L);
		this.specialRegisters.put("ID/EX.B", 0L);
		this.specialRegisters.put("ID/EX.Imm", 0L);
		this.specialRegisters.put("ID/EX.IR", 0L);
		this.specialRegisters.put("ID/EX.NPC", 0L);

		this.specialRegisters.put("EX/MEM.ALUOutput", 0L);
		this.specialRegisters.put("EX/MEM.Cond", 0L);
		this.specialRegisters.put("EX/MEM.IR", 0L);
		this.specialRegisters.put("EX/MEM.B", 0L);

		this.specialRegisters.put("MEM/WB.ALUOutput", 0L);
		this.specialRegisters.put("MEM/WB.LMD", 0L);
		this.specialRegisters.put("MEM/WB.IR", 0L);
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
