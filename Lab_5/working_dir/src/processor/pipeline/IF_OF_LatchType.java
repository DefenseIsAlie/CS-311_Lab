package processor.pipeline;

public class IF_OF_LatchType {
	boolean OF_busy;
	boolean OF_enable;
	int I; // instruction
	
	// constructor
	public IF_OF_LatchType() {
		this.OF_enable = false;
	}

	// getter and setter for OF_enable
	public boolean isOF_enable() {
		return this.OF_enable;
	}
	public void setOF_enable(boolean OF_enable) {
		this.OF_enable = OF_enable;
	}

	// getter and setter for intruction I
	public int getInstruction() {
		return this.I;
	}
	public void setInstruction(int I) {
		this.I = I;
	}

}
