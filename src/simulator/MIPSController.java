package simulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import models.Program;
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
		
		window.setVisible(true);
		window.getCode().setText(document);
	}
	
	public void stepAction(){
	    simulator.step();
	    window.repaint();
	}
	
	public void loadAction(){	    
	    Program program = new Program(window.getCode().getText());
	    simulator.loadProgram(program);
	    
	    // Update tables
	    window.getRegisters().setModel(simulator.getRegisters().getRegistersTableModel());
	    window.getSpecialRegisters().setModel(simulator.getRegisters().getSpecialRegistersTableModel());
	    window.getMemory().setModel(simulator.getMemory().getMemoryTableModel());
	    window.getMemory().getRowSorter().toggleSortOrder(0);
	    window.repaint();
	}

}
