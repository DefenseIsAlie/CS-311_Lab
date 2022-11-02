package processor.pipeline;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	String OPCODE;
	int rs1,rs2,rd,imm;
	int rs1addr,rs2addr;
	int PC;
	boolean isNOP;
	boolean isBusy;
	
	public OF_EX_LatchType() {
		EX_enable = false;
		OPCODE = "70000";
		rs1 = 70000;
		rs2 = 70000;
		rd = 70000;
		imm = 70000;
		PC = -1;
		isNOP = false;
		rs1addr = 45;
		rs2addr = 45;
		isBusy = false;
	}

	public boolean getIsNOP() {
		return this.isNOP;
	}
	public void setIsNOP(boolean isNOP) {
		this.isNOP = isNOP;
	}
	
 	public int getPC() {
		return this.PC;
	}
	public void setPC(int PC) {
		this.PC = PC;
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

 	public boolean getIsBusy() {
		return this.isBusy;
	}
	public void setIsBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}

	public String toString() {
		return "OF_EX_LatchType";
	}

	public boolean comparePC (int pc) {
		return PC == pc;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}
	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}
	
}
