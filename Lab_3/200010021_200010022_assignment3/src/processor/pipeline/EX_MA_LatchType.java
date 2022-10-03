package processor.pipeline;

import generic.Instruction;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	int ALUresult; // result obtained by the execute phase
	Instruction I; // instruction packet

	public EX_MA_LatchType(){
		this.MA_enable = false;
	}

	// getter and setter for MA_enable
	public boolean isMA_enable() {
		return this.MA_enable;
	}
	public void setMA_enable(boolean MA_enable) {
		this.MA_enable = MA_enable;
	}

	// getter and setter for instruction packet
	public Instruction getInstruction() {
		return this.I;
	}
	public void setInstruction(Instruction I) {
		this.I = I;
	}

	// getter and setter for the ALUresult obtained from EX phase
	public int getALU_result() {
		return this.ALUresult;
	}
	public void setALU_result(int ALUresult) {
		this.ALUresult = ALUresult;
	}

}