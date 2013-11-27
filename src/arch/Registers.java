package arch;

public class Registers {
	private long[] registers_;
	
	public Registers(){
		this.registers_ = new long[0x20];
	}
	
	public long getRegister(int number){
		if(number == 0)
//			For R0
			return 0;
		return this.registers_[number];
	}
	
	public void setRegister(int number, int value) {
		if(number != 0)
			this.registers_[number] = value;
	}
}
