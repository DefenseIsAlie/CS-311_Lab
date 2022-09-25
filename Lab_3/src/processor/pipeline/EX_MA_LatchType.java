package processor.pipeline;

import generic.Instruction;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	int ALU_result;
	Instruction I;

	// constructor for the class EX_MAX_Latchtype
	public EX_MA_LatchType(){
		MA_enable = false;
	}

	// getter and setter for MA_enable
	public boolean isMA_enable() {
		return this.MA_enable;
	}
	public void setMA_enable(boolean MA_enable) {
		this.MA_enable = MA_enable;
	}

	// getter and setter for instruction object I
	public Instruction getInstruction() {
		return this.I;
	}
	public void setInstruction(Instruction I) {
		this.I = I;
	}

	// getter and setter for the ALU_result
	public int getALU_result() {
		return this.ALU_result;
	}
	public void setALU_result(int ALU_result) {
		this.ALU_result = ALU_result;
	}
}