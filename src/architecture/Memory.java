package architecture;

public class Memory {
	private int[] memory_;
	
	public Memory(){
//		Declare size to be 2000 units
//		The first 1000 (0x0000 to 0x0FFF) is the code segment
//		Rest is main memory
		this.memory_ = new int[0x2000];
	}
	
	public int getMemoryAddress(int address) {
		return this.memory_[address];
	}
	
	public void setMemoryAddress(int address, int value){
		this.memory_[address] = value;
	}
}
