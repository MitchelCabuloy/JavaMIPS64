package architecture;

import java.util.HashMap;
import java.util.Map.Entry;

public class Registers {
	private long[] registers;
	private HashMap<String, Long> specialRegisters;
	private HashMap<Object, Long> transactions;

	public Registers() {
		this.registers = new long[0x20];
		this.specialRegisters = new HashMap<String, Long>();

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
		if(number == 0){
			// Silently fail if someone is trying to set R0
		}
		else if (number > 0 && number < 32)
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
		for(Entry<String, Long> entry : this.specialRegisters.entrySet()){
			System.out.println(String.format("%s: %016x", entry.getKey(), entry.getValue()));
		}
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
