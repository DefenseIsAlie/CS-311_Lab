package processor.pipeline;

import generic.Instruction;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	Instruction I;

	public OF_EX_LatchType()
	{
		EX_enable = false;
	}

	// getter and setter for EX_enable
	public boolean isEX_enable() {
		return this.EX_enable;
	}
	public void setEX_enable(boolean EX_enable) {
		this.EX_enable = EX_enable;
	}

	// getter and setter for the object I of class Instruction
	public Instruction getInstruction() {
		return this.I;
	}
	public void setInstruction(Instruction I) {
		this.I = I;
	}

}
