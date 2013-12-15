package util;

import java.nio.ByteBuffer;

public class ByteUtils {
	private static ByteBuffer buffer = ByteBuffer.allocate(8);

	public static byte[] longToBytes(long x) {
		buffer.putLong(0, x);
		return buffer.array();
	}

	public static long bytesToLong(byte[] bytes) {
		buffer.put(bytes, 0, bytes.length);
		buffer.flip();// need flip
		return buffer.getLong();
	}
	
	public static long getUnsignedInt(int x) {
	    return x & 0x00000000FFFFFFFFL;
	}

	public static String getBinaryString(Integer integer) {
		return String.format("%32s", Integer.toBinaryString(integer))
				.replace(" ", "0");
	}

	public static int getOpcode(Integer codeSegment) {
		String RS = getBinaryString(codeSegment);
		return Integer.parseInt(RS.substring(0, 6), 2);
	}

	public static int getRS(Integer codeSegment) {
		String RS = getBinaryString(codeSegment);
		return Integer.parseInt(RS.substring(6, 11), 2);
	}

	public static int getRT(Integer codeSegment) {
		String RS = getBinaryString(codeSegment);
		return Integer.parseInt(RS.substring(11, 16), 2);
	}

	public static int getRD(Integer codeSegment) {
		String RS = getBinaryString(codeSegment);
		// System.out.println(RS.substring(16, 32));
		return Integer.parseInt(RS.substring(16, 21), 2);
	}

	public static int getImm(Integer codeSegment) {
		String RS = getBinaryString(codeSegment);
		if(RS.substring(16, 17).equals("1")){
			RS = "1111111111111111" + RS.substring(16, 32);
		}
		return (int) Long.parseLong(RS, 2);
	}

	public static int getFunc(Integer codeSegment) {
		String RS = getBinaryString(codeSegment);
		return Integer.parseInt(RS.substring(26, 32), 2);
	}
	
	public static int getJOffset(Integer codeSegment) {
		String RS = getBinaryString(codeSegment);
		return Integer.parseInt(RS.substring(6, 32), 2);
	}
}
