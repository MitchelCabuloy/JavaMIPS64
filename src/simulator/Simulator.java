package simulator;

import java.math.BigInteger;
import java.util.Map.Entry;

import models.Code;
import models.Program;
import util.ByteUtils;
import architecture.ALU;
import architecture.Memory;
import architecture.Registers;
import exceptions.MemoryOutOfRangeException;
import exceptions.RegisterOutOfBoundsException;

public class Simulator {
	private Memory memory;
	private Registers registers;

	public Simulator() {
		this.memory = new Memory();
		this.registers = new Registers();
	}

	public Memory getMemory() {
		return memory;
	}

	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	public Registers getRegisters() {
		return registers;
	}

	public void setRegisters(Registers registers) {
		this.registers = registers;
	}

	public void loadProgram(Program program) throws RegisterOutOfBoundsException, MemoryOutOfRangeException {
		memory = new Memory();
		registers = new Registers();

		// Load registers
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

			registers.setRegister(
					Integer.parseInt(register.replaceAll("R(\\d+)", "$1")),
					value);
		}

		// Load memory
		for (Entry<Integer, Byte> entry : program.getMemory().entrySet()) {
			memory.setMemoryAddress(entry.getKey(), entry.getValue());
		}

		// TODO: Place code that saves the code to the code segment here
		// You can get the code in program.code
		int lineNumber = 0;
		for (Code code : program.getCodes()) {
			// int codeSegment = Decoder.decode(code);
			memory.setCodeSegment(lineNumber, code.getInstruction());
			code.setAddress(lineNumber * 4);
			lineNumber++;
		}

		// // Debugging code while decoder not yet implemented
		// this.memory.setCodeSegment(0, 0x00231025); // OR R3, R1, R2

		// Save changes to registers
		registers.commit();

		// Save changes to memory
		memory.commit();

		// System.out.println("Initial");
		// registers.seeRegisters();
		// this.memory.seeMemory();
	}

	public void step() throws RegisterOutOfBoundsException, MemoryOutOfRangeException {
		// Instruction fetch
		// Set IF/ID.IR to Memory[PC]
		registers.setRegister("IF/ID.IR",
				memory.getCodeSegment((int) (registers.getRegister("PC") / 4)));
		// Set PC <- PC + 4
		registers.setRegister("PC", registers.getRegister("PC") + 4);
		// Pipeline #2 doesn't have NPC

		// Instruction decode
		registers.setRegister("ID/EX.IR", registers.getRegister("IF/ID.IR"));
		registers.setRegister("ID/EX.A", registers.getRegister(ByteUtils
				.getRS((int) registers.getRegister("IF/ID.IR"))));
		registers.setRegister("ID/EX.B", registers.getRegister(ByteUtils
				.getRT((int) registers.getRegister("IF/ID.IR"))));
		registers.setRegister("ID/EX.Imm",
				ByteUtils.getImm((int) registers.getRegister("IF/ID.IR")));
		// Set PC here because Pipeline #2
		if (ByteUtils.getOpcode((int) registers.getRegister("IF/ID.IR")) == 3) { // J
			registers.setRegister("PC", ByteUtils.getJOffset((int) registers
					.getRegister("IF/ID.IR")) * 4);
		} else if (ByteUtils.getOpcode((int) registers.getRegister("IF/ID.IR")) == 5
				&& ByteUtils.getRS((int) registers.getRegister("IF/ID.IR")) != 0) { // BNEZ
			registers.setRegister(
					"PC",
					registers.getRegister("PC")
							+ (ByteUtils.getImm((int) registers
									.getRegister("IF/ID.IR")) * 4));
		}

		// Execute
		registers.setRegister("EX/MEM.ALUOUTPUT", ALU.getOutput(
				registers.getRegister("ID/EX.IR"), registers, memory));
		registers.setRegister("EX/MEM.IR", registers.getRegister("ID/EX.IR"));
		registers.setRegister("EX/MEM.B", registers.getRegister("ID/EX.B"));

		// Memory
		switch (ByteUtils.getOpcode((int) registers.getRegister("EX/MEM.IR"))) {
		case 55: // LD
			registers.setRegister("MEM/WB.LMD", Memory.getLMD(
					registers.getRegister("EX/MEM.IR"), registers, memory));
			break;
		}
		registers.setRegister("MEM/WB.ALUOUTPUT",
				registers.getRegister("EX/MEM.ALUOUTPUT"));
		registers.setRegister("MEM/WB.IR", registers.getRegister("EX/MEM.IR"));

		// Write back
		switch (ByteUtils.getOpcode((int) registers.getRegister("MEM/WB.IR"))) {
		case 0: // R-Type. Save ALU Output in RD
			registers.setRegister(
					ByteUtils.getRD((int) registers.getRegister("MEM/WB.IR")),
					registers.getRegister("MEM/WB.ALUOUTPUT"));
			break;
		case 24: // DADDI. Save ALU Output in RT
			registers.setRegister(
					ByteUtils.getRT((int) registers.getRegister("MEM/WB.IR")),
					registers.getRegister("MEM/WB.ALUOUTPUT"));
			break;
		case 55: // LD. Save LMD to RT
			registers.setRegister(
					ByteUtils.getRT((int) registers.getRegister("MEM/WB.IR")),
					registers.getRegister("MEM/WB.LMD"));
			break;
		}

		registers.commit();
		memory.commit();

		// System.out.println("STEP");
		// this.registers.seeRegisters();
		// this.memory.seeMemory();
	}
}
