package util;

import exceptions.RegisterOutOfBoundsException;

public class ExceptionUtils {
	public static int validateRegister(int register)
			throws RegisterOutOfBoundsException {
		if (register >= 0 && register < 32)
			return register;
		throw new RegisterOutOfBoundsException(register);
	}
}
