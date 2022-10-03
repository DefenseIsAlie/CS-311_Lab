package processor.pipeline;

import generic.Instruction;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	Instruction I;
	int loadResult;
	int ALUresult;
	
	public MA_RW_LatchType(){
		this.RW_enable = false;
	}

	// getter and setter for RW_enable
	public boolean isRW_enable() {
		return this.RW_enable;
	}
	public void setRW_enable(boolean RW_enable) {
		this.RW_enable = RW_enable;
	}

	// getter and setter for the instruction packet
	public Instruction getInstruction() {
		return this.I;
	}
	public void setInstruction(Instruction I) {
		this.I = I;
	}

	// getter and setter for the loadResult
	public void setLoad_result(int loadResult) {
		this.loadResult = loadResult;
	}
	public int getLoad_result() {
		return this.loadResult;
	}

	// getter and setter for the ALUresult
	public int getALU_result() {
		return this.ALUresult;
	}
	public void setALU_result(int ALUresult) {
		this.ALUresult = ALUresult;
	}
}
