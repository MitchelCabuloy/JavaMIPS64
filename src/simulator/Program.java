package simulator;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Program {
	private Map<String, Long> registers;
	private Map<Integer, Integer> memory;
	private ArrayList<String> code;
	
	public Program(String document){
		loadYAML(document);
	}
	
	public Program(FileInputStream document){
		loadYAML(document);
	}

	private void loadYAML(Object document) {
		Yaml yaml = new Yaml();

		// FileInputStream document = null;
		// try {
		// document = new FileInputStream(new File("sample/example.yaml"));
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		if (document instanceof FileInputStream || document instanceof String) {
			Map map = null;
			if (document instanceof FileInputStream)
				map = (Map) yaml.load((FileInputStream) document);
			else
				map = (Map) yaml.load((String) document);
			this.registers = (LinkedHashMap) map.get("registers");
			this.memory = (LinkedHashMap) map.get("memory");

			this.code = new ArrayList<String>(Arrays.asList(((String) map
					.get("code")).split("\n")));
		}

		// Print
		// System.out.println(registers);
		// System.out.println(memory);
		// for (String line : code) {
		// System.out.println(line);
		// }
	}
}
