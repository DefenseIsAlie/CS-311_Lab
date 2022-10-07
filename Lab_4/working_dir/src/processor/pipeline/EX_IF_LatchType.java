package processor.pipeline;

public class EX_IF_LatchType {
	// every instruction after the execute stage has computed the newPC
	boolean IS_enable;
	int PC; // program counter 
	
	// constructor for this latch type
	public EX_IF_LatchType() {
		this.IS_enable = false;
	}

	// parametrized constructor 
	public EX_IF_LatchType(boolean iS_enable) {
		IS_enable = iS_enable;
	}

	// getter and setter for IS_enable boolean
	public boolean getIS_enable() {
		return this.IS_enable;
	}
	public void setIS_enable(boolean IS_enable) {
		this.IS_enable = IS_enable;
	}

	// setter for IS_enable boolean and PC
	public void setIS_enable(boolean IS_enable, int PC) {
		this.IS_enable = IS_enable;
		this.PC = PC;
	}

	// getter for the PC
	public int getPC() {
		return this.PC;
	}

}
