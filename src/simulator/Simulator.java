package simulator;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map.Entry;

import models.Code;
import models.PipelineMapTableModel;
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
	private boolean squashFlag;
	private int stallCounter;
	private int cycleCount;
	private HashMap<Long, String> pipeline;
	private PipelineMapTableModel pipelineMapTableModel;

	// public Simulator() {
	// this.memory = new Memory();
	// this.registers = new Registers();
	// }

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

	public HashMap<Long, String> getPipeline() {
		return pipeline;
	}

	public void setPipeline(HashMap<Long, String> pipeline) {
		this.pipeline = pipeline;
	}

	public PipelineMapTableModel getPipelineMapTableModel() {
		return this.pipelineMapTableModel;
	}

	public void loadProgram(Program program)
			throws RegisterOutOfBoundsException, MemoryOutOfRangeException {
		memory = new Memory();
		registers = new Registers();
		squashFlag = false;
		stallCounter = 0;
		cycleCount = 0;
		pipeline = new HashMap<Long, String>();
		pipelineMapTableModel = new PipelineMapTableModel(program.getCodes());

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

	public void step() throws RegisterOutOfBoundsException,
			MemoryOutOfRangeException {
		// Detect stall here, and set stallCounter = 3
		if (isStalled()) {
			stallCounter = 3;
		}

		if (squashFlag) {
			// Remove last cycle's IF/ID.IR
			registers.setRegister("IF/ID.IR", 0L);
			registers.commit();
			squashFlag = false;
		}

		// Instruction fetch
		if (stallCounter == 0) {
			// Set IF/ID.IR to Memory[PC]
			registers.setRegister("IF/ID.IR", ByteUtils.getUnsignedInt(memory
					.getCodeSegment((int) (registers.getRegister("PC") / 4))));
			// Set PC <- PC + 4
			registers.setRegister("PC", registers.getRegister("PC") + 4);
			// Pipeline #2 doesn't have NPC

			// Instruction decode
			registers
					.setRegister("ID/EX.IR", registers.getRegister("IF/ID.IR"));
			registers.setRegister("ID/EX.A", registers.getRegister(ByteUtils
					.getRS((int) registers.getRegister("IF/ID.IR"))));
			registers.setRegister("ID/EX.B", registers.getRegister(ByteUtils
					.getRT((int) registers.getRegister("IF/ID.IR"))));
			registers.setRegister("ID/EX.Imm",
					ByteUtils.getImm((int) registers.getRegister("IF/ID.IR")));

			// Set PC here because Pipeline #2
			if (ByteUtils.getOpcode((int) registers.getRegister("IF/ID.IR")) == 2) { // J
				registers.setRegister("PC",
						ByteUtils.getJOffset((int) registers
								.getRegister("IF/ID.IR")) * 4);
				// Since we're branching, squash the IF/ID.IR
				squashFlag = true;
			} else if (ByteUtils.getOpcode((int) registers
					.getRegister("IF/ID.IR")) == 5
					&& ByteUtils.getRS((int) registers.getRegister("IF/ID.IR")) != 0) { // BNEZ
				registers.setRegister(
						"PC",
						registers.getRegister("PC")
								+ (ByteUtils.getImm((int) registers
										.getRegister("IF/ID.IR")) * 4));
				// Since we're branching, squash the IF/ID.IR
				squashFlag = true;
			}

		} else {
			// Clear out ID/EX.IR registers so EX won't read them next cycle.
			registers.setRegister("ID/EX.IR", 0L);
			registers.setRegister("ID/EX.A", 0L);
			registers.setRegister("ID/EX.B", 0L);
			registers.setRegister("ID/EX.Imm", 0L);
			// stallCounter--;
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
		case 63: // SD
			memory.putDouble((int) registers.getRegister("EX/MEM.ALUOUTPUT"),
					registers.getRegister(ByteUtils.getRT((int) registers
							.getRegister("EX/MEM.IR"))));
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

		updatePipeline();
		
		if(stallCounter != 0){
			stallCounter--;
		}

		registers.commit();
		memory.commit();
		cycleCount++;

		// System.out.println("STEP");
		// this.registers.seeRegisters();
		// this.memory.seeMemory();
	}

	private boolean isStalled() throws RegisterOutOfBoundsException {

		long IDEX_RD = ByteUtils.getRD((int) registers.getRegister("ID/EX.IR"));
		long IDEX_RT = ByteUtils.getRT((int) registers.getRegister("ID/EX.IR"));
		long IFID_RS = ByteUtils.getRS((int) registers.getRegister("IF/ID.IR"));
		long IFID_RT = ByteUtils.getRT((int) registers.getRegister("IF/ID.IR"));

		// If J, no dep
		if (ByteUtils.getOpcode((int) registers.getRegister("IF/ID.IR")) == 2)
			return false;

		// No dependencies if we're dealing with R0
		// If R-Type
		if (IDEX_RD != 0) {
			// if there's a dependency
			if (IDEX_RD == IFID_RS || IDEX_RD == IFID_RT) {
				return true;
			}
		}

		// If I-Type
		if (IDEX_RT != 0) {
			if (ByteUtils.getOpcode((int) registers.getRegister("ID/EX.IR")) == 24
					|| ByteUtils.getOpcode((int) registers
							.getRegister("ID/EX.IR")) == 55) {
				if (IDEX_RT == IFID_RS || IDEX_RT == IFID_RT) {
					return true;
				}
			}

			// if (IDEX_RT == IFID_RS || IDEX_RT == IFID_RT) {
			// return true;
			// }
		}

		return false;

		// return true;
	}

	private void updatePipeline() throws RegisterOutOfBoundsException {
		long IFID_IR = ByteUtils.getUnsignedInt(memory
				.getCodeSegment((int) (registers.getRegister("PC") / 4)));

		// If there's something in there
		if (IFID_IR != 0) {
			// Add it to the pipeline
			// pipeline.put(IFID_IR, "IF");
			if (stallCounter != 0) {
				pipeline.put(IFID_IR, "*");
			} else {
				pipeline.put(IFID_IR, "IF");
			}
		}

		long IDEX_IR = registers.getRegister("IF/ID.IR");

		// If there's something in there
		if (IDEX_IR != 0) {
			// Add it to the pipeline
			if (stallCounter != 0) {
				pipeline.put(IDEX_IR, "*");
			} else {
				pipeline.put(IDEX_IR, "ID");
			}
		}

		long EXMEM_IR = registers.getRegister("ID/EX.IR");

		// If there's something in there
		if (EXMEM_IR != 0) {
			// Add it to the pipeline
			pipeline.put(EXMEM_IR, "EX");
		}

		long MEMWB_IR = registers.getRegister("EX/MEM.IR");

		// If there's something in there
		if (MEMWB_IR != 0) {
			// Add it to the pipeline
			pipeline.put(MEMWB_IR, "MEM");
		}

		long WB_IR = registers.getRegister("MEM/WB.IR");

		// If there's something in there
		if (WB_IR != 0) {
			// Add it to the pipeline
			pipeline.put(WB_IR, "WB");
		}

		pipelineMapTableModel.updateData(cycleCount, pipeline);

		pipeline.clear();
	}

	public boolean isRunning() throws RegisterOutOfBoundsException{
		long IFID_IR = ByteUtils.getUnsignedInt(memory
				.getCodeSegment((int) (registers.getRegister("PC") / 4)));

		long IDEX_IR = registers.getRegister("IF/ID.IR");

		long EXMEM_IR = registers.getRegister("ID/EX.IR");

		long MEMWB_IR = registers.getRegister("EX/MEM.IR");

		long WB_IR = registers.getRegister("MEM/WB.IR");
		
		if(IFID_IR == 0 && IDEX_IR == 0 && EXMEM_IR == 0 && MEMWB_IR == 0 && WB_IR == 0)
			return false;
		return true;
	}
}
