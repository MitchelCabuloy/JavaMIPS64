package models;

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
	private ArrayList<String> codeStrings;
	private ArrayList<Code> codes;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Program(String document) throws Exception {
		Yaml yaml = new Yaml();

		Map map = (Map) yaml.load((String) document);

		registers = (LinkedHashMap) map.get("registers");

		memory = new HashMap<Integer, Byte>();
		Map<Integer, Integer> memoryHash = (LinkedHashMap) map.get("memory");
		for (Entry<Integer, Integer> entry : memoryHash.entrySet()) {
			memory.put(entry.getKey(), entry.getValue().byteValue());
		}

		codeStrings = new ArrayList<String>(Arrays.asList(((String) map
				.get("code")).split("\n")));

		codes = new ArrayList<Code>();

		for (String codeString : codeStrings) {
			codes.add(new Code(codeString));
		}

	}

	public Map<String, Object> getRegisters() {
		return this.registers;
	}

	public Map<Integer, Byte> getMemory() {
		return this.memory;
	}

	public ArrayList<String> getCodeStrings() {
		return this.codeStrings;
	}

	public ArrayList<Code> getCodes() {
		return codes;
	}
}
