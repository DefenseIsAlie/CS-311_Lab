package processor.pipeline;

public class co_unit {
	int instruction;
	String opcode="",rs1="",rs2="",rd="",Imm = "";
	public boolean r3(String OP) {
		if(OP.equals("")) return false;
		if(OP.charAt(4)=='0' && !(OP.charAt(0)=='1' && OP.charAt(1)=='1') && !OP.equals("10110"))
			return true;
		else return false;
	}
	public boolean r2i(String OP) {
		if(OP.equals("")) return false;
		if( ((OP.charAt(4)=='1') || OP.equals("10110") || OP.equals("11010") ||OP.equals("11100") ) && !OP.equals("11101"))
			return true;
		else return false;
		
	}
	public boolean ri(String OP) {
		if(OP.equals("")) return false;
		if(OP.equals("11101") || OP.equals("11000"))
			return true;
		else return false;
	}
	public void setInstruction(int instruction) {
		this.instruction = instruction;
		String instructionString = Integer.toBinaryString(instruction);
		int size = instructionString.length();
		String str="" ;
		for (int i=0;i<32-size;i++){
			str = str + "0" ;
		}
		instructionString = str + instructionString;
		rs1 = instructionString.substring(5,10);
		rs2 = instructionString.substring(10,15);
		opcode=instructionString.substring(0,5);
		switch(String.valueOf(r3(opcode)))
		{
			case "true":
			rd=instructionString.substring(15,20);
		}
		switch(String.valueOf(r2i(opcode)))
		{
			case "true":
			rd=instructionString.substring(10,15);
			Imm=instructionString.substring(15,32);
		}
		switch(String.valueOf(ri(opcode)))
		{
			case "true":
			rd=instructionString.substring(5,10);
			Imm=instructionString.substring(10,32);
		}
	} 
	public boolean isWb(){
		if ((opcode.charAt(0) == '1' && opcode.charAt(1) =='1') || (opcode.equals("10111"))){
			return false;
		}
		return true;
	}
	public boolean isimm(){
		if (( opcode.charAt(4)=='1'|| opcode.equals("10110")) && !( opcode.equals("11101") || opcode.equals("11001") || opcode.equals("11011"))){
			return true;
		}
		else return false ;
	}
	public boolean isSt(){
		if(opcode.equals("10111")){
			return true;
		}
		else return false ;
	}
}
