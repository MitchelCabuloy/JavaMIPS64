package exceptions;

public class CodeSegmentOutOfRangeException extends RuntimeException {
	public CodeSegmentOutOfRangeException() {
		super("Code segment entered is out of range");
	}

	public CodeSegmentOutOfRangeException(int lineNumber) {
		super("Code segment " + lineNumber + " is out of range");
	}
}