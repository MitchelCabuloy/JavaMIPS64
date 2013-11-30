package simulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Map.Entry;

import architecture.Memory;
import architecture.Registers;

public class MIPSController {
	public static void main(String[] args) {
		new MIPSController();
	}

	private Memory memory;
	private Registers registers;

	public MIPSController() {
		this.memory = new Memory();
		this.registers = new Registers();

		FileInputStream document = null;
		try {
			document = new FileInputStream(new File("sample/example.yaml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Program program = new Program(document);

		loadProgram(program);

		this.registers.seeRegisters();
		this.memory.seeMemory();
	}

	private void loadProgram(Program program) {

		for (Entry<String, Object> entry : program.getRegisters().entrySet()) {
			String register = entry.getKey();
			Long value = 0L;

			// We need because YAML loads data based on their size, but we
			// ALWAYS need a Long value.
			if (entry.getValue() instanceof Integer)
				value = new Long((Integer) entry.getValue());
			else if (entry.getValue() instanceof Long)
				value = (Long) entry.getValue();
			else if (entry.getValue() instanceof BigInteger)
				value = ((BigInteger) entry.getValue()).longValue();

			try {
				this.registers.setRegister(
						Integer.parseInt(register.replaceAll("R(\\d+)", "$1")),
						value);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		for (Entry<Integer, Integer> entry : program.getMemory().entrySet()) {
			Integer address = entry.getKey();
			Integer value = entry.getValue();

			this.memory.setMemoryAddress(address, value);
		}

		// Save changes to registers
		this.registers.commit();

		// Save changes to memory
		this.memory.commit();
	}
}
