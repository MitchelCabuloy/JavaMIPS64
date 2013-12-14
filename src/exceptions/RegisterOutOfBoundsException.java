package exceptions;

public class RegisterOutOfBoundsException extends RuntimeException {
	public RegisterOutOfBoundsException() {
	}

	public RegisterOutOfBoundsException(String register) {
		super(String.format("Register %s is not a valid register", register));
	}

	public RegisterOutOfBoundsException(int number) {
		super(String.format("Register R%d is not a valid register", number));
	}
}