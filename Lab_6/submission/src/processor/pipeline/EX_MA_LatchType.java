package processor.pipeline;

public class EX_MA_LatchType {
	
	// data memebers
	boolean MA_enable;
	int aluResult;
	int rs1,rs2,rd,imm;
	int rs1addr,rs2addr;
	String opcode;
	int insPC;
	boolean isNop;
	boolean isBusy;
	
	// constructor
	public EX_MA_LatchType()
	{
		MA_enable = false;
		opcode = "70000";
		rs1 = 70000;
		rs2 = 70000;
		rd = 70000;
		imm = 70000;
		aluResult = 70000;
		insPC = -1;
		isNop = false;
		rs1addr = 45;
		rs2addr = 45;
		isBusy = false;
	}

	// data functions
	public boolean getIsNOP() {
		return this.isNop;
	}
	public void setIsNOP(boolean isNop) {
		this.isNop = isNop;
	}

	public int getALUresult() {
		return this.aluResult;
	}
	public void setALUresult(int aluResult) {
		this.aluResult = aluResult;
	}

	public int getIMM() {
		return this.imm;
	}
	public void setIMM(int imm) {
		this.imm = imm;
	}

	public String getOPcode() {
		return this.opcode;
	}
	public void setOPcode(String opcode) {
		this.opcode = opcode;
	}

	public int getRS1() {
		return this.rs1;
	}
	public void setRS1(int rs1) {
		this.rs1 = rs1;
	}

	public int getRS2() {
		return this.rs2;
	}
	public void setRS2(int rs2) {
		this.rs2 = rs2;
	}

	public int getRD() {
		return this.rd;
	}
	public void setRD(int rd) {
		this.rd = rd;
	}

	public boolean getIsBusy() {
		return this.isBusy;
	}
	public void setIsBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}

	public int getPC() {
		return this.insPC;
	}
	public void setPC(int insPC) {
		this.insPC = insPC;
	}

	public boolean isMA_enable() {
		return this.MA_enable;
	}
	public void setMA_enable(boolean MA_enable) {
		this.MA_enable = MA_enable;
	}

}
