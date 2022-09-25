package processor.pipeline;

public class EX_IF_LatchType {

	boolean isEnable;
	int PC;
	
	// constructor for class EX_IF_LatchType
	public EX_IF_LatchType(){
		this.isEnable = false;
	}

	// getter and setter for isEnable and PC
	public boolean getIS_enable() {
		return this.isEnable;
	}
	public void setIS_enable(boolean isEnable, int PC) {
		this.isEnable = isEnable;
		this.PC = PC;
	}
	public void setIS_enable(boolean isEnable) {
		this.isEnable = isEnable;
	}
	public int getPC() {
		return PC;
	}
}