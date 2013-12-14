package exceptions;

@SuppressWarnings("serial")
public class MemoryOutOfRangeException extends Exception {
	public MemoryOutOfRangeException() {
		super("Memory address entered is beyond the 0x1000 to 0x1FFF range");
	};

	public MemoryOutOfRangeException(int address) {
		super(String.format(
				"Memory address %04x is beyond the 0x1000 to 0x1FFF range",
				address));
	}
}