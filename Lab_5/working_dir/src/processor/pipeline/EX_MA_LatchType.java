package processor.pipeline;

public class EX_MA_LatchType {
	
	boolean MA_enable,MA_busy;
	int ALUresult;
	int op2;
	int rd;
	int I;
	
	public EX_MA_LatchType() {
		MA_enable = false;
	}

	// getters and setter for each data member
	
	public void setMA_busy(boolean MA_busy){
		this.MA_busy = MA_busy;
	}
	public boolean isMA_busy() {
		return this.MA_busy;
	}

	public boolean isMA_enable() {
		return this.MA_enable;
	}
	public void setMA_enable(boolean MA_enable) {
		this.MA_enable = MA_enable;
	}	

	public int getop2(){
		return this.op2;
	}
	public void setop2(int op2){
		this.op2 = op2;
	}

	public int getaluRes(){
		return this.ALUresult;
	}
	public void setaluRes(int ALUresult){
		this.ALUresult = ALUresult;
	}

	public int getrd() {
		return this.rd;
	}
	public void setrd(int rd) {
		this.rd=rd;
	}

	public int getInstruction() {
		return I;
	}
	public void setInstruction(int I){
		this.I = I;
	}
}