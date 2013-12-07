package simulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Map.Entry;

import util.ByteUtils;
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

		// Default file
		// Debugging only
		FileInputStream document = null;
		try {
			document = new FileInputStream(new File("sample/example.yaml"));
		} catch (FileNotFoundException e) {
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

		for (Entry<Integer, Byte> entry : program.getMemory().entrySet()) {
			this.memory.setMemoryAddress(entry.getKey(), entry.getValue());
		}

		// Save changes to registers
		this.registers.commit();

		// Save changes to memory
		this.memory.commit();
	}

	private void step() {
		// Instruction fetch
		// Set IF/ID.IR to Memory[PC]
		registers.setRegister("IF/ID.IR",
				memory.getCodeSegment((int) (registers.getRegister("PC") / 4)));
		// Pipeline #2 doesn't have NPC

		// Instruction decode
		long ID_IR = registers.getRegister("IF/ID.IR"); // previous value
		registers.setRegister("ID/EX.IR", ID_IR);
		registers.setRegister("ID/EX.A", ByteUtils.getRS((int) ID_IR));
		registers.setRegister("ID/EX.B", ByteUtils.getRT((int) ID_IR));
		registers.setRegister("ID/EX.Imm", ByteUtils.getImm((int) ID_IR));
		
		// Execute
		
		
		// Memory
		
		
		// Write back
		
		
		
		registers.commit();
		memory.commit();
	}
}
