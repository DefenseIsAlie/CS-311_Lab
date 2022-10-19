package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable;
	int I; // instruction

	/* need to check this part once again */
	
	// constructor
	public IF_OF_LatchType() {
		OF_enable = false;
	}

	// getter and setter for OF_enable
	public void setOF_enable(boolean OF_enable) {
		this.OF_enable = OF_enable;
	}
	public boolean isOF_enable() {
		return this.OF_enable;
	}

	// getter and setter for instruction I
	public int getInstruction() {
		return this.I;
	}
	public void setInstruction(int I) {
		this.I = I;
	}
}
