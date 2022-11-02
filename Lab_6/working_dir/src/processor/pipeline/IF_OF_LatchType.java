package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable;
	int I;
	int insPC;
	boolean isBusy;
	
	public IF_OF_LatchType() {
		OF_enable = false;
		insPC = -1;
		I = -1999;
		isBusy = false;
	}
	public IF_OF_LatchType(boolean oF_enable, boolean isBusy) {
		OF_enable = oF_enable;
		insPC = -1;
		I = -1999;
		this.isBusy = isBusy;
	}

	public int getPC() {
		return this.insPC;
	}
	public void setPC(int PC) {
		this.insPC = PC;
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
		return this.insPC == PC;
	}

	public int getInstruction() {
		return this.I;
	}
	public void setInstruction(int I) {
		this.I = I;
	}
}
