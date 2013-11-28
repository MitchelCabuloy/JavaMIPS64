package simulator;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Map.Entry;

import architecture.Memory;
import architecture.Registers;

public class MIPSController {
	private Memory memory;
	private Registers registers;
	
	public MIPSController(){
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
	}
	
	private void loadProgram(Program program){
		
		for(Entry<String, Object> entry : program.getRegisters().entrySet()){
			String register = entry.getKey();
			Long value = 0L;
			
			if(entry.getValue() instanceof Integer)
				value = new Long((Integer) entry.getValue());
			else if(entry.getValue() instanceof Long)
				value = (Long) entry.getValue();
			else if(entry.getValue() instanceof BigInteger)
				value = ((BigInteger)entry.getValue()).longValue();
			
			try {
				this.registers.setRegister(Integer.parseInt(register.replaceAll("R(\\d+)", "$1")), value);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
