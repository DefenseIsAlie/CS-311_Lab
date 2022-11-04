package processor.pipeline;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	int ALUresult;
	int rs1,rs2,rd,imm;
	int rs1addr,rs2addr;
	String OPCODE;
	int PC;
	boolean isLoad;
	boolean isNOP;
	int load_result;
	
	public MA_RW_LatchType() {
		RW_enable = false;
		OPCODE = "70000";
		rs1 = 70000;
		rs2 = 70000;
		rd = 70000;
		imm = 70000;
		ALUresult = 70000;
		PC = -1;
		isLoad =  false;
		isNOP = false;
		rs1addr = 45;
		rs2addr = 45;
	}
	
	public boolean getIsLoad() {
		return this.isLoad;
	}
	public void setIsLoad(boolean isLoad) {
		this.isLoad = isLoad;
	}

	public boolean getIsNOP() {
		return this.isNOP;
	}
	public void setIsNOP(boolean isNOP) {
		this.isNOP = isNOP;
	}

	public int getALUresult() {
		return this.ALUresult;
	}
	public void setALUresult(int ALUresult) {
		this.ALUresult = ALUresult;
	}

	public int getIMM() {
		return this.imm;
	}
	public void setIMM(int imm) {
		this.imm = imm;
	}

	public String getOPcode() {
		return this.OPCODE;
	}
	public void setOPcode(String OPCODE) {
		this.OPCODE = OPCODE;
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

	public int getPC() {
		return this.PC;
	}
	public void setPC(int PC) {
		this.PC = PC;
	}

	public String toString() {
		return "MA_RW_LatchType";
	}

	public boolean isRW_enable() {
		return RW_enable;
	}
	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}

	public void setLoad_result(int result) {
		load_result = result;
	}
	public int getLoad_result() {
		return load_result;
	}
}
