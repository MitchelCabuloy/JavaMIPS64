package simulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import util.StreamUtils;
import views.Main;

public class MIPSController {
	public static void main(String[] args) {
		new MIPSController();
	}

	private Simulator simulator;
	private Main window;

	public MIPSController() {
		this.simulator = new Simulator();
		this.window = new Main(this);

		// Default file
		// Debugging only
		String document = null;
		try {
			document = StreamUtils.getStringFromInputStream(new FileInputStream(new File("sample/example.yaml")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

//		Program program = new Program(document);
		
		window.setVisible(true);
		window.getCode().setText(document);
//		this.simulator.loadProgram(program);
		
//		for(int i = 0; i < 5; i++){
//			this.simulator.step();
//		}
	}
	
	public void stepAction(){
	    simulator.step();
	}
	
	public void loadAction(){	    
	    Program program = new Program(window.getCode().getText());
	    simulator.loadProgram(program);
	}

}
