package simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.yaml.snakeyaml.Yaml;

public class Program {
	private Map<String, Object> registers;
	private Map<Integer, Byte> memory;
	private ArrayList<String> code;

	public Program(String document) {
		Yaml yaml = new Yaml();

		Map map = (Map) yaml.load((String) document);

		this.registers = (LinkedHashMap) map.get("registers");

		this.memory = new HashMap<Integer, Byte>();
		Map<Integer, Integer> memoryHash = (LinkedHashMap) map.get("memory");
		for (Entry<Integer, Integer> entry : memoryHash.entrySet()) {
			this.memory.put(entry.getKey(), entry.getValue().byteValue());
		}

		this.code = new ArrayList<String>(Arrays.asList(((String) map
				.get("code")).split("\n")));

	}

	public Map<String, Object> getRegisters() {
		return this.registers;
	}

	public Map<Integer, Byte> getMemory() {
		return this.memory;
	}

	public ArrayList<String> getCode() {
		return this.code;
	}
}
