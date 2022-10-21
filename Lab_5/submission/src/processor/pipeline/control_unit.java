package processor.pipeline;

public class control_unit {
	String OPcode;
	String immediate;

	public void setopcode(String OPcode){
		this.OPcode = OPcode;
	}
	public String getopcode(){
		return this.OPcode;
	}

	public boolean isSt(){
		// 10111 is the OPcode of store instruction
		if(OPcode.equals("10111")) return true;
		else return false ;
	}
	public boolean isimm1(){
		if ((!OPcode.equals("11101") && OPcode.charAt(4)=='1') || OPcode.equals("10110") || OPcode.equals("11010") ){
			return true;
		}
		else return false ;
	}
	public boolean isimm(){
		if (( OPcode.charAt(4)=='1'|| OPcode.equals("10110")) && !( OPcode.equals("11101") || OPcode.equals("11001") || OPcode.equals("11011"))){
			return true;
		}
		else return false ;
	}
	public boolean isLd(){
		if (OPcode.equals("10110")){
			return true;
		}
		return false ;
	}
	public boolean isWb(){
		if ((OPcode.charAt(0) == '1' && OPcode.charAt(1) =='1') || (OPcode.equals("10111"))){
			return false;
		}
		return true;
	}
	public boolean isend(){
		if(OPcode.equals("11101")){
			return true;
		}
		return false;
	}
}
