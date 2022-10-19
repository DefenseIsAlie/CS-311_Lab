package processor.pipeline;

import generic.Instruction;

public class EX_MA_LatchType {
	
	boolean MA_enable; // is MA stage enable ?
	int ALUresult; // result obtained from the execute stage
	boolean isNOP; // is the instruction a NOP ?
	Instruction I; // instruction packet

	// construction for this latch type
	public EX_MA_LatchType() {
		MA_enable = false;
		this.isNOP = false;
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

	// getter and setter for isNOP boolean
	public boolean getIsNOP() {
		return this.isNOP;
	}
	public void setIsNOP(boolean isNOP) {
		this.isNOP = isNOP;
	}
}
