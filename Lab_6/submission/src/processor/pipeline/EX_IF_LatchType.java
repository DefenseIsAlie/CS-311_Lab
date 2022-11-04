package processor.pipeline;

public class EX_IF_LatchType {
	
	// data members
	boolean isBranchTaken;
	int offset;
	boolean IS_enable;
	int PC;
	
	// constructor
	public EX_IF_LatchType() {
		isBranchTaken = false;
		offset = 70000;
	}

	// data function
	public boolean getIsBranchTaken() {
		return this.isBranchTaken;
	}
	public void setIsBranchTaken(boolean isBranchTaken) {
		this.isBranchTaken = isBranchTaken;
	}

	public int getOffset() {
		return this.offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}

	public boolean getIS_enable() {
		return IS_enable;
	}
	public void setIS_enable(boolean IS_enable) {
		this.IS_enable = IS_enable;
	}
	public void setIS_enable(boolean IS_enable, int PC) {
		this.IS_enable = IS_enable;
		this.PC = PC;
	}

	public int getPC() {
		return this.PC;
	}
	public void setPc(int PC) {
		this.PC = PC;
	}
}
