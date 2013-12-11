package simulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MIPSController {
	public static void main(String[] args) {
		new MIPSController();
	}

	private Simulator simulator;

	public MIPSController() {
		this.simulator = new Simulator();

		// Default file
		// Debugging only
		FileInputStream document = null;
		try {
			document = new FileInputStream(new File("sample/example.yaml"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Program program = new Program(document);

		this.simulator.loadProgram(program);
		
		for(int i = 0; i < 5; i++){
			this.simulator.step();
		}
	}

}
