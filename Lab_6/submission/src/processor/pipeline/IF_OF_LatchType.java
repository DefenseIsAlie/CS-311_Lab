package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable;
	int I;
	int PC;
	boolean isBusy;
	
	// constructor
	public IF_OF_LatchType() {
		OF_enable = false;
		PC = -1;
		I = -1999;
		isBusy = false;
	}
	
	// parametrized constructor
	public IF_OF_LatchType(boolean oF_enable, boolean isBusy) {
		OF_enable = oF_enable;
		PC = -1;
		I = -1999;
		this.isBusy = isBusy;
	}

	public int getPC() {
		return this.PC;
	}
	public void setPC(int PC) {
		this.PC = PC;
	}

	public boolean isOF_enable() {
		return OF_enable;
	}
	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}

	public boolean checkInstruction(int I) {
		return this.I == I;
	}
	public boolean checkPC(int PC) {
		return this.PC == PC;
	}

	public int getInstruction() {
		return this.I;
	}
	public void setInstruction(int I) {
		this.I = I;
	}

	public boolean getIsBusy() {
		return this.isBusy;
	}
	public void setIsBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}
}
