package processor.pipeline;

public class EX_IF_LatchType {

	boolean isBranchEnable;
	int PC;
	
	public EX_IF_LatchType(){
		isBranchEnable = false;
	}

	// getter and setter of isBranchEnable
	public boolean getIsBranchEnable() {
		return isBranchEnable;
	}
	public void setIsBranchEnable(boolean isBranchEnable) {
		this.isBranchEnable = isBranchEnable;
	}
	
	// getter and setter for PC
	public int getPC() {
		return PC;
	}
	public void setPC(int PC) {
		this.PC = PC;
	}

}