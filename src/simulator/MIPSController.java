package simulator;


import architecture.Memory;
import architecture.Registers;

public class MIPSController {
	private Memory memory;
	private Registers registers;
	
	public MIPSController(){
		this.memory = new Memory();
		this.registers = new Registers();
	}
}
