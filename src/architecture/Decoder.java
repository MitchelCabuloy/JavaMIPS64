package architecture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.ByteUtils;
import util.ExceptionUtils;
import exceptions.InvalidSyntaxException;
import exceptions.RegisterOutOfBoundsException;

public class Decoder {
	private final static Pattern[] patterns = {
			Pattern.compile("^[ \\t]*(L\\d{1,2}: )?(DADD) R(\\d{1,2}), R(\\d{1,2}), R(\\d{1,2})$"),
			Pattern.compile("^[ \\t]*(L\\d{1,2}: )?(DSUB) R(\\d{1,2}), R(\\d{1,2}), R(\\d{1,2})$"),
			Pattern.compile("^[ \\t]*(L\\d{1,2}: )?(OR) R(\\d{1,2}), R(\\d{1,2}), R(\\d{1,2})$"),
			Pattern.compile("^[ \\t]*(L\\d{1,2}: )?(XOR) R(\\d{1,2}), R(\\d{1,2}), R(\\d{1,2})$"),
			Pattern.compile("^[ \\t]*(L\\d{1,2}: )?(SLT) R(\\d{1,2}), R(\\d{1,2}), R(\\d{1,2})$"),
			Pattern.compile("^[ \\t]*(L\\d{1,2}: )?(BNEZ) R(\\d{1,2}), (L\\d)$"),
			Pattern.compile("^[ \\t]*(L\\d{1,2}: )?(BNEZ) R(\\d{1,2}), (#[0-9a-fA-F]{1,4})$"),
			Pattern.compile("^[ \\t]*(L\\d{1,2}: )?(LD) R(\\d{1,2}), ([0-9a-fA-F]{1,4})\\(R(\\d{1,2})\\)$"),
			Pattern.compile("^[ \\t]*(L\\d{1,2}: )?(SD) R(\\d{1,2}), ([0-9a-fA-F]{1,4})\\(R(\\d{1,2})\\)$"),
			Pattern.compile("^[ \\t]*(L\\d{1,2}: )?(DADDI) R(\\d{1,2}), R(\\d{1,2}), (#[0-9a-fA-F]{1,4})$"),
			Pattern.compile("^[ \\t]*(L\\d{1,2}: )?(J) (L\\d)$"),
			Pattern.compile("^[ \\t]*(L\\d{1,2}: )?(J) (#[0-9a-fA-F]{1,4})$") };

	private final static HashMap<String, String> instructions = instructionsInitializer();

	private static HashMap<String, String> instructionsInitializer() {
		HashMap<String, String> tempMap = new HashMap<String, String>();
		tempMap.put("DADD", "000000");
		tempMap.put("DSUB", "000000");
		tempMap.put("OR", "000000");
		tempMap.put("XOR", "000000");
		tempMap.put("SLT", "000000");

		tempMap.put("BNEZ", "000101");
		tempMap.put("LD", "110111");
		tempMap.put("SD", "111111");
		tempMap.put("DADDI", "011000");
		tempMap.put("J", "000010");

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

		return tempMap;
	}

	public static int decode(String codeString,
			HashMap<Integer, Integer> labels, int lineNumber)
			throws InvalidSyntaxException, RegisterOutOfBoundsException {
		for (Pattern pattern : patterns) {
			Matcher m = pattern.matcher(codeString);

			if (m.find()) {
				StringBuilder sb = new StringBuilder();

				// Get opcode
				sb.append(instructions.get(m.group(2).toUpperCase()));

				switch (m.group(2).toUpperCase()) {
				case "DADD":
				case "DSUB":
				case "OR":
				case "XOR":
				case "SLT":
					// R-Type

					// RS
					sb.append(ByteUtils.getBinaryString(
							ExceptionUtils.validateRegister(Integer.parseInt(m
									.group(4)))).substring(27));

					// RT
					sb.append(ByteUtils.getBinaryString(
							ExceptionUtils.validateRegister(Integer.parseInt(m
									.group(5)))).substring(27));

					// RD
					sb.append(ByteUtils.getBinaryString(
							ExceptionUtils.validateRegister(Integer.parseInt(m
									.group(3)))).substring(27));

					// 00000
					sb.append("00000");

					// Func
					sb.append(functions.get(m.group(2)));

					break;
				case "BNEZ":
					// RS
					sb.append(ByteUtils.getBinaryString(
							ExceptionUtils.validateRegister(Integer.parseInt(m
									.group(3)))).substring(27));

					// 00000
					sb.append("00000");

					// Imm
					// If Label
					if (m.group(4).toUpperCase().contains("L")) {
						// TODO: do something smart
						sb.append(ByteUtils.getBinaryString(
								labels.get(Integer.parseInt(m.group(4)
										.substring(1))) - (lineNumber + 1))
								.substring(16));
					} else if (m.group(4).contains("#")) { // Line number
						sb.append(ByteUtils.getBinaryString(
								Integer.parseInt(m.group(4).substring(1), 16))
								.substring(16));
					}
					break;
				case "LD":
				case "SD": // they have the same format
					// RS
					sb.append(ByteUtils.getBinaryString(
							ExceptionUtils.validateRegister(Integer.parseInt(m
									.group(5)))).substring(27));

					// RT
					sb.append(ByteUtils.getBinaryString(
							ExceptionUtils.validateRegister(Integer.parseInt(m
									.group(3)))).substring(27));

					// Offset
					sb.append(ByteUtils.getBinaryString(
							Integer.parseInt(m.group(4), 16)).substring(16));
					break;
				case "DADDI":
					// RS
					sb.append(ByteUtils.getBinaryString(
							ExceptionUtils.validateRegister(Integer.parseInt(m
									.group(4)))).substring(27));

					// RD
					sb.append(ByteUtils.getBinaryString(
							ExceptionUtils.validateRegister(Integer.parseInt(m
									.group(3)))).substring(27));

					// IMM
					sb.append(ByteUtils.getBinaryString(
							Integer.parseInt(m.group(5).substring(1), 16))
							.substring(16));
					break;
				case "J":
					if (m.group(3).toUpperCase().contains("L")) {
						// TODO: do something smart with L
						sb.append(ByteUtils.getBinaryString(
								labels.get(Integer.parseInt(m.group(3)
										.substring(1)))).substring(6));
					} else {
						sb.append(ByteUtils.getBinaryString(
								Integer.parseInt(m.group(3).substring(1), 16))
								.substring(6));
					}
					break;
				}

				// Use Long to avoid overflows
				return (int) Long.parseLong(sb.toString(), 2);
			}
		}

		throw new InvalidSyntaxException(codeString);
	}

	public static HashMap<Integer, Integer> parseLabels(
			ArrayList<String> codeStrings) {
		HashMap<Integer, Integer> labels = new HashMap<Integer, Integer>();

		int lineNumber = 0;
		for (String codeString : codeStrings) {
			for (Pattern pattern : patterns) {
				Matcher m = pattern.matcher(codeString);

				if (m.find()) {
					if (m.group(1) != null) {
						labels.put(
								Integer.parseInt(m.group(1).substring(1,
										m.group(1).length() - 2)), lineNumber);
					}
				}
			}

			lineNumber++;
		}

		return labels;
	}
}