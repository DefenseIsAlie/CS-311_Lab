package processor.pipeline;

import generic.Instruction;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	Instruction I; // instruction packet
	boolean isNOP;

	// constructor of this latch
	public OF_EX_LatchType() {
		this.EX_enable = false;
		this.isNOP = false;
	}

	// getter and setter for EX_enable
	public boolean isEX_enable() {
		return this.EX_enable;
	}
	public void setEX_enable(boolean EX_enable) {
		this.EX_enable = EX_enable;
	}

	// getter and setter for the instruction packet
	public Instruction getInstruction() {
		return this.I;
	}
	public void setInstruction(Instruction I) {
		this.I = I;
	}
	
	// getter and setter for isNOP
	public boolean getIsNOP() {
		return this.isNOP;
	}
	public void setIsNOP(boolean isNOP) {
		this.isNOP = isNOP;
	}

}
