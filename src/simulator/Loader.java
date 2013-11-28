package simulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Loader {
	public Loader() {
		Yaml yaml = new Yaml();
		
		FileInputStream document = null;
		try {
			document = new FileInputStream(new File("sample/example.yaml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Map map = (Map) yaml.load(document);
		Map<String, Long> registers = (LinkedHashMap) map.get("registers");
		Map<Integer, Integer> memory = (LinkedHashMap) map.get("memory");

		ArrayList<String> code = new ArrayList<String>(
				Arrays.asList(((String) map.get("code")).split("\n")));
		
		
//		Print
		System.out.println(registers);
		System.out.println(memory);
		for (String line : code) {
			System.out.println(line);
		}
	}
}
