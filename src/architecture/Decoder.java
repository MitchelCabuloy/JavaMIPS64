package architecture;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exceptions.InvalidSyntaxException;
import util.ByteUtils;

public class Decoder {
	public static int decode(String code) {
		int result = 0b00000000000000000000000000000000;
		String type = "";

		int opcode_size = 6;
		int rs_size = 5;
		int rt_size = 5;
		int rd_size = 5;
		int zero_size = 5;
		int func_size = 6;
		int immediate_size = 16;
		int offset_size = 26;

		int IR_size = 32;

		int i = 0;
		int command_opcode = 0;
		int command_func = 0;
		int rs = 0;
		int rt = 0;
		int rd = 0;
		int imm = 0;
		int label = 0;
		int zero = 0;

		String delims = "[, ()#]+";
		String[] tokens = code.split(delims);

		// To-do While loop
		switch (tokens[i]) {
		case "DADD":
			command_func = 44;
			type = "R";
			break;

		case "DSUB":
			command_func = 46;
			type = "R";
			break;

		case "OR":
			command_func = 37;
			type = "R";
			break;

		case "XOR":
			command_func = 38;
			type = "R";
			break;

		case "SLT":
			command_func = 42;
			type = "R";
			break;

		case "BNEZ":
			command_opcode = 5;
			type = "I";
			break;

		case "LD":
			command_opcode = 55;
			type = "I";
			break;

		case "SD":
			command_opcode = 63;
			type = "I";
			break;

		case "DADDI":
			command_opcode = 24;
			type = "I";
			break;

		case "J":
			command_opcode = 2;
			type = "J";
			break;

		default:
			System.out.println("Command not found");
		}

		switch (type) {
		case "R":
			if (tokens.length == 4) {
				for (i += 1; i < tokens.length; i++) {
					if (tokens[i].contains("R")) {
						if (i == 1)
							rd = Integer.parseInt(tokens[i].substring(1));

						else if (i == 2)
							rs = Integer.parseInt(tokens[i].substring(1));

						else if (i == 3)
							rt = Integer.parseInt(tokens[i].substring(1));
					}

				}

				result |= (command_opcode << IR_size - opcode_size);
				IR_size -= opcode_size;
				result |= (rs << IR_size - rs_size);
				IR_size -= rs_size;
				result |= (rt << IR_size - rt_size);
				IR_size -= rt_size;
				result |= (rd << IR_size - rd_size);
				IR_size -= rd_size;
				result |= (zero << IR_size - zero_size);
				IR_size -= zero_size;
				result |= (command_func << IR_size - func_size);
				IR_size -= func_size;
			}

			break;

		case "I":
			if (tokens.length == 3) {
				for (i += 1; i < tokens.length; i++) {
					if (tokens[i].contains("R")) {
						if (i == 1)
							rd = Integer.parseInt(tokens[i].substring(1));

						else if (i == 2)
							rs = Integer.parseInt(tokens[i].substring(1));

						else if (i == 3)
							imm = Integer.parseInt(tokens[i].substring(1));
					} else if (Integer.parseInt(tokens[i]) > 0) {
						imm = Integer.parseInt(tokens[i]);
					}
				}
			} else if (tokens.length == 4 && tokens[i].equals("DADDI")) {
				for (i += 1; i < tokens.length; i++) {
					if (tokens[i].contains("R")) {
						if (i == 1)
							rd = Integer.parseInt(tokens[i].substring(1));

						else if (i == 2)
							rs = Integer.parseInt(tokens[i].substring(1));
					} else if (Integer.parseInt(tokens[i]) > 0) {
						imm = Integer.parseInt(tokens[i]);
					}
				}
			}

			result |= (command_opcode << IR_size - opcode_size);
			IR_size -= opcode_size;
			result |= (rs << IR_size - rs_size);
			IR_size -= rs_size;
			result |= (rd << IR_size - rt_size);
			IR_size -= rt_size;
			result |= (imm << IR_size - immediate_size);
			IR_size -= immediate_size;
			break;

		case "J":
			if (tokens.length == 2) {
				for (i += 1; i < tokens.length; i++) {
					if (tokens[i].contains("L")) {
						label = Integer.parseInt(tokens[i].substring(1));
					}

				}

				result |= (command_opcode << IR_size - opcode_size);
				IR_size -= opcode_size;
				result |= (label << IR_size - offset_size);
				IR_size -= offset_size;
			}
			break;
		}

		return result;
	}

	private final static Pattern[] patterns = {
			Pattern.compile("^[ \\t]*(DADD) R(\\d{1,2}), R(\\d{1,2}), R(\\d{1,2})$"),
			Pattern.compile("^[ \\t]*(DSUB) R(\\d{1,2}), R(\\d{1,2}), R(\\d{1,2})$"),
			Pattern.compile("^[ \\t]*(OR) R(\\d{1,2}), R(\\d{1,2}), R(\\d{1,2})$"),
			Pattern.compile("^[ \\t]*(XOR) R(\\d{1,2}), R(\\d{1,2}), R(\\d{1,2})$"),
			Pattern.compile("^[ \\t]*(SLT) R(\\d{1,2}), R(\\d{1,2}), R(\\d{1,2})$"),
			Pattern.compile("^[ \\t]*(BNEZ) R(\\d{1,2}), (L\\d)$"),
			Pattern.compile("^[ \\t]*(BNEZ) R(\\d{1,2}), (#\\d{1,4})$"),
			Pattern.compile("^[ \\t]*(LD) R(\\d{1,2}), (\\d{1,4})\\(R(\\d{1,2})\\)$"),
			Pattern.compile("^[ \\t]*(SD) R(\\d{1,2}), (\\d{1,4})\\(R(\\d{1,2})\\)$"),
			Pattern.compile("^[ \\t]*(DADDI) R(\\d{1,2}), R(\\d{1,2}), (#\\d{1,4})$"),
			Pattern.compile("^[ \\t]*(J) (L\\d)$"),
			Pattern.compile("^[ \\t]*(J) (#\\d{1,4})$") };

	private final static HashMap<String, String> instructions = instructionsInitializer();

	private static HashMap<String, String> instructionsInitializer() {
		HashMap<String, String> tempMap = new HashMap<String, String>();
		tempMap.put("DADD", "000000");
		tempMap.put("DSUB", "000000");
		tempMap.put("OR", "000000");
		tempMap.put("XOR", "000000");
		tempMap.put("SLT", "000000");
		
		return tempMap;
	}

	private final static HashMap<String, String> functions = functionsInitializer();

	private static HashMap<String, String> functionsInitializer() {
		HashMap<String, String> tempMap = new HashMap<String, String>();
		tempMap.put("DADD", "101100");
		tempMap.put("DSUB", "101110");
		tempMap.put("OR", "100101");
		tempMap.put("XOR", "100110");
		tempMap.put("SLT", "101010");
		
		tempMap.put("BNEZ", "000101");
		tempMap.put("LD", "110111");
		tempMap.put("SD", "111111");
		tempMap.put("DADDI", "011000");
		tempMap.put("J", "000010");
		
		return tempMap;
	}

	public static int decode2(String codeString) throws InvalidSyntaxException {
		for (Pattern pattern : patterns) {
			Matcher m = pattern.matcher(codeString);

			if (m.find()) {
				StringBuilder sb = new StringBuilder();
				
				// Get opcode
				sb.append(instructions.get(m.group(0).toUpperCase()));
				
				switch (m.group(0).toUpperCase()) {
				case "DADD":
				case "DSUB":
				case "OR":
				case "XOR":
				case "SLT":
					// R-Type

					// RS
					sb.append(ByteUtils.getBinaryString(
							Integer.parseInt(m.group(2))).substring(27));

					// RT
					sb.append(ByteUtils.getBinaryString(
							Integer.parseInt(m.group(3))).substring(27));

					// RD
					sb.append(ByteUtils.getBinaryString(
							Integer.parseInt(m.group(1))).substring(27));

					// 00000
					sb.append("00000");

					// Func
					sb.append(functions.get(m.group(0)));

					break;
				case "BNEZ":
					// RS
					sb.append(ByteUtils.getBinaryString(
							Integer.parseInt(m.group(1))).substring(27));

					// 00000
					sb.append("00000");
					
					// Imm
					// If Label
					if (m.group(2).toUpperCase().contains("L")) {
						m.group(2).substring(1);
						// TODO: do something smart
						sb.append("0000000000000000");
					} else if (m.group(2).contains("#")) { // Line number
						sb.append(ByteUtils.getBinaryString(Integer.parseInt(m.group(2), 16))
								.substring(16));
					}
					break;
				case "LD":
				case "SD": // they have the same format
					// RS
					sb.append(ByteUtils.getBinaryString(
							Integer.parseInt(m.group(3))).substring(27));
					
					// RT
					sb.append(ByteUtils.getBinaryString(
							Integer.parseInt(m.group(1))).substring(27));
					
					// Offset
					sb.append(ByteUtils.getBinaryString(
							Integer.parseInt(m.group(2))).substring(16));
					break;
				case "DADDI":
					// RS
					sb.append(ByteUtils.getBinaryString(
							Integer.parseInt(m.group(2))).substring(27));
					
					// RD
					sb.append(ByteUtils.getBinaryString(
							Integer.parseInt(m.group(1))).substring(27));
					
					// IMM
					sb.append(ByteUtils.getBinaryString(
							Integer.parseInt(m.group(3).substring(1), 16)).substring(16));
					break;
				case "J":
					if(m.group(1).toUpperCase().contains("L")){
						// TODO: do something smart with L
						sb.append("00000000000000000000000000");
					} else {
						sb.append(ByteUtils.getBinaryString(Integer.parseInt(m.group(1), 16))
								.substring(16));
					}
					break;
				}
				
				return Integer.parseInt(sb.toString(), 2);
			}
		}
		
		throw new InvalidSyntaxException(codeString);
	}
}