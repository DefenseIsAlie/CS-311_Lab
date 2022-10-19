package processor.pipeline;

public class MA_RW_LatchType {
	boolean RW_enable;
	int ALUresult;
	int loadResult;
	int rd;
	int I ;
	
	// public constructor
	public MA_RW_LatchType() {
		this.RW_enable = false;
	}

	// getters and setters for data members
	public boolean isRW_enable() {
		return this.RW_enable;
	}
	public void setRW_enable(boolean RW_enable) {
		this.RW_enable = RW_enable;
	}
	
	public int getalures(){
		return this.ALUresult;
	}
	public void setalures(int ALUresult){
		this.ALUresult = ALUresult;
	}

	public int getldres(){
		return this.loadResult;
	}
	public void setldres(int loadResult){
		this.loadResult = loadResult;
	}
	
	public int getrd(){
		return this.rd;
	}
	public void setrd(int rd){
		this.rd = rd;
	}
	
	public int getInstruction() {
		return this.I;
	}
	public void setInstruction(int I) {
		this.I = I;
	}
}
