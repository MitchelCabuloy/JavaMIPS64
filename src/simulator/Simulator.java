package simulator;

import java.math.BigInteger;
import java.util.Map.Entry;

import util.ByteUtils;
import architecture.ALU;
import architecture.Memory;
import architecture.Registers;

public class Simulator {
	private Memory memory;
	private Registers registers;

	public Simulator() {
		this.memory = new Memory();
		this.registers = new Registers();
	}

	public void loadProgram(Program program) {

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

			try {
				this.registers.setRegister(
						Integer.parseInt(register.replaceAll("R(\\d+)", "$1")),
						value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Load memory
		for (Entry<Integer, Byte> entry : program.getMemory().entrySet()) {
			this.memory.setMemoryAddress(entry.getKey(), entry.getValue());
		}

		// TODO: Place code that saves the code to the code segment here
		// You can get the code in program.code
		// int lineNumber = 0;
		// for(String code : program.getCode()){
		// int codeSegment = Decoder.decode(code);
		// this.memory.setCodeSegment(lineNumber, codeSegment);
		// lineNumber++;
		// }

		// Save changes to registers
		this.registers.commit();

		// Save changes to memory
		this.memory.commit();
	}

	public void step() {
		// Instruction fetch
		// Set IF/ID.IR to Memory[PC]
		registers.setRegister("IF/ID.IR",
				memory.getCodeSegment((int) (registers.getRegister("PC") / 4)));
		// Pipeline #2 doesn't have NPC

		// Instruction decode
		registers.setRegister("ID/EX.IR", registers.getRegister("IF/ID.IR"));
		registers.setRegister("ID/EX.A",
				ByteUtils.getRS((int) registers.getRegister("IF/ID.IR")));
		registers.setRegister("ID/EX.B",
				ByteUtils.getRT((int) registers.getRegister("IF/ID.IR")));
		registers.setRegister("ID/EX.Imm",
				ByteUtils.getImm((int) registers.getRegister("IF/ID.IR")));

		// Execute
		registers.setRegister("EX/MEM.ALUOUTPUT", ALU.getOutput(
				registers.getRegister("ID/EX.IR"), registers, memory));
		registers.setRegister("EX/MEM.IR", registers.getRegister("ID/EX.IR"));
		registers.setRegister("EX/MEM.B", registers.getRegister("ID/EX.B"));

		// Memory
		switch(ByteUtils.getOpcode((int) registers.getRegister("EX/MEM.IR"))){
		case 55: // LD
			registers.setRegister("MEM/WB.LMD", memory.getLMD(registers.getRegister("EX/MEM.IR"), registers, memory));
			break;
		}
		registers.setRegister("MEM/WB.ALUOUTPUT",
				registers.getRegister("EX/MEM.ALUOUTPUT"));
		registers.setRegister("MEM/WB.IR", registers.getRegister("EX/MEM.IR"));

		// Write back
		// Depends on command

		registers.commit();
		memory.commit();
	}

}
