package processor.pipeline;

public class OF_EX_LatchType {
	
	boolean EX_enable,EX_busy;
	int immediate;
	int branchTarget;
	int op1;
	int op2;
	int destinationRegister;
	int I; // instruction

	public OF_EX_LatchType() {
		this.EX_enable = false;
	}

	public void setEX_busy(boolean EX_busy){
		this.EX_busy = EX_busy;
	}
	public boolean isEX_busy() {
		return this.EX_busy;
	}

	public boolean isEX_enable() {
		return this.EX_enable;
	}
	public void setEX_enable(boolean EX_enable) {
		this.EX_enable = EX_enable;
	}

	public void setimmx(int immediate) {
		this.immediate = immediate;
	}
	public int getimmx() {
		return this.immediate;
	}

	public void setbranchtarget(int branchTarget) {
		this.branchTarget = branchTarget;
	}
	public int getbranchtarget() {
		return this.branchTarget;
	}

	public int getoperand1() {
		return this.op1;
	}	
	public void setoperand1(int op1) {
		this.op1 = op1;
	}

	public int getoperand2() {
		return this.op2;
	}
	public void setoperand2(int op2) {
		this.op2 = op2;
	}	

	public void setrd(int destinationRegister) {
		this.destinationRegister = destinationRegister;
	}
	public int getrd() {
		return this.destinationRegister;
	}
	
	public int getInstruction() {
		return this.I;
	}
	public void setInstruction(int I) {
		this.I = I;
	}
}
