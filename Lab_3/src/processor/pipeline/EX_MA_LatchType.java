package processor.pipeline;

import generic.Instruction;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	Instruction I;
	int ALU_result;
	
	public EX_MA_LatchType()
	{
		this.MA_enable = false;
	}

	// getter and setter for isMA_enable
	public boolean isMA_enable() {
		return this.MA_enable;
	}
	public void setMA_enable(boolean MA_enable) {
		this.MA_enable = MA_enable;
	}

	// getter and setter for the object of the Instruction class
	public Instruction getInstruction() {
		return this.I;
	}
	public void setInstrucion(Instruction I) {
		this.I = I;
	}

	// getter and setter for the ALU_result obtained from the execute stage
	public int getALU_result() {
		return this.ALU_result;
	} 
	public void setALU_result(int ALU_result) {
		this.ALU_result = ALU_result;
	}
}
