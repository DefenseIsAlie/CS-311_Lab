package processor.pipeline;

import generic.Instruction;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	Instruction I;
	int ALU_result;
	int loadResult;

	public MA_RW_LatchType()
	{
		this.RW_enable = false;
	}

	// getter and setter for isRW_enable
	public boolean isRW_enable() {
		return this.RW_enable;
	}
	public void setRW_enable(boolean RW_enable) {
		this.RW_enable = RW_enable;
	}

	// getter and setter for the object of class Instruction
	public Instruction getInstruction() {
		return this.I;
	}
	public void setInstruction(Instruction I) {
		this.I = I;
	}

	// getter and setter for the ALU_result variable
	public int getALU_result() {
		return this.ALU_result;
	}
	public void setALU_result(int ALU_result) {
		this.ALU_result = ALU_result;
	}

	// getter and setter for the loadResult variable
	public int getLoadResult() {
		return this.loadResult;
	}
	public void setLoadResult(int loadResult) {
		this.loadResult = loadResult;
	}
}
