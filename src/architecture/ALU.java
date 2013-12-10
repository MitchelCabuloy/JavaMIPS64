package architecture;

import util.ByteUtils;

public class ALU {
	public static long getOutput(long IR, Registers registers, Memory memory) {
		switch (ByteUtils.getOpcode((int) IR)) {
		// R Type
		case 0:
			switch (ByteUtils.getFunc((int) IR)) {

			case 38: // XOR
				return registers.getRegister("ID/EX.A")
						^ registers.getRegister("ID/EX.B");
			case 44: // DADD
				return registers.getRegister("ID/EX.A")
						+ registers.getRegister("ID/EX.B");
			}
			break;

		case 55: // LD
		case 63: // SD
			return ByteUtils.getImm((int) IR)
					+ registers.getRegister("ID/EX.A");
		}

		return 0L;
	}
}
