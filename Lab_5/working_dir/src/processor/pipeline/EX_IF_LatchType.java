package processor.pipeline;

public class EX_IF_LatchType {
	boolean IF_enable,isBranchTaken;
	int branchTarget;
	
	public EX_IF_LatchType(){
		this.IF_enable=false;
		this.isBranchTaken=false;
	}

	public void setisbranchtaken(){
		this.isBranchTaken=true;
	}
	public void setbranchtarget(int branchTarget){
		this.branchTarget = branchTarget;
	}
	public void setIF_enable(boolean IF_enable){
		this.IF_enable = IF_enable;
	}
}
