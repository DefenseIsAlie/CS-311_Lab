package processor.pipeline;

import generic.Statistics;
import processor.Processor;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	co_unit controlunit = new co_unit();
	boolean hasEnded = false;
	
	public boolean R3(String OP) {
		if(OP.equals("")) return false;

		if(OP.charAt(4)=='0' && !(OP.charAt(0)=='1' && OP.charAt(1)=='1') && !OP.equals("10110")) {
			return true;
		} else {
			return false;
		}
	}
	public boolean R2I(String OP) {
		if(OP.equals("")) return false;
		if( ((OP.charAt(4)=='1') || OP.equals("10110") || OP.equals("11010") ||OP.equals("11100") ) && !OP.equals("11101")) {
			return true;
		} else {
			return false;
		}
		
	}
	public boolean R2I1(String OP) {
		if(OP.equals("")) return false;
		if(OP.charAt(4)=='1'  && !(OP.charAt(0)=='1' && OP.charAt(1)=='1') && !OP.equals("10111")) {
			return true;
		} else {
			return false;
		}
	}
	public boolean R2I2(String OP) {
		if(OP.equals("")) return false;
		if((OP.equals("10110") || OP.equals("10111"))) {
			return true;
		} else {
			return false;
		}
	}
	public boolean R2I3(String OP) { 
		if(OP.equals("")) return false;
		if((OP.charAt(0)=='1' && OP.charAt(1)=='1')&&!OP.equals("11000")&&!OP.equals("11101")  )
			return true;
		else return false;
	}
	public boolean RI(String OP) {
		if(OP.equals("")) return false;
		if(OP.equals("11101") || OP.equals("11000")) {
			return true;
		} else {
			return false;
		}
	}

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}
	
	public int binaryConversion(String str) {
		int size = str.length();
		int i = Integer.parseInt(str,2);
		if (str.charAt(0) == '1') {
			i = i - (int)Math.pow(2, size);
			return i ;
		}
		else return i ;
	}
	
	public void performOF()
	{
		if(IF_OF_Latch.isOF_enable() && !hasEnded) {
			if(OF_EX_Latch.isEX_busy()) {
				IF_OF_Latch.OF_busy=true;
				return;
			}
			else {
				IF_OF_Latch.OF_busy=false;
			}
			int I = IF_OF_Latch.getInstruction(); // raw instruction of size 1 word
			controlunit.setInstruction(I);
			String binaryInstruction = Integer.toBinaryString(I);
			int size = binaryInstruction.length();
			String str="";
			for (int i =0;i <32-size;i ++){
				str = str + "0" ;
			}
			binaryInstruction = str + binaryInstruction;
			
			String opcode="",rs1="",rs2="",rd="",immx = "";
			rs1 = binaryInstruction.substring(5,10);
			rs2 = binaryInstruction.substring(10,15);
			opcode=binaryInstruction.substring(0,5);
			
			if (binaryInstruction.substring(0,5).equals("11101")) {
				hasEnded = true;
				containingProcessor.getIFUnit().hasEnded=true;
			}
			
			switch(String.valueOf(R3(opcode)))
			{
				case "true":
					rd=binaryInstruction.substring(15,20);
					immx=binaryInstruction.substring(20,32);
					break;
			}
			switch(String.valueOf(R2I(opcode)))
			{
				case "true":
					rd=binaryInstruction.substring(10,15);
					immx=binaryInstruction.substring(15,32);
					break;
			}
			switch(String.valueOf(RI(opcode)))
			{
				case "true":
					rd=binaryInstruction.substring(5,10);
					immx=binaryInstruction.substring(10,32);
					break;
			}
		
			String rp1 ;
			String rp2 ;
			if(!(opcode.equals("10111"))){ 
				rp1 = rs1;
				rp2 = rs2;
			}
			else{
				rp1 = rd;
				rp2 = rs1;
			}
			int operand1 = containingProcessor.getRegisterFile().getValue( Integer.parseInt(rp1,2) );
			int operand2 = containingProcessor.getRegisterFile().getValue( Integer.parseInt(rp2,2) );
			
			boolean conf1 = false;
			boolean conf2 = false;
			boolean conf3 = false;
			
			switch(String.valueOf(R3(opcode)))
			{
				case "true":
					if(containingProcessor.getEXUnit().controlunit.opcode.equals("00111") || containingProcessor.getEXUnit().controlunit.opcode.equals("00110")) {
						if(rs1.equals("11111")){
							conf1 = true;
						}
						if(rs2.equals("11111")){
							conf1 = true;
						}
					}
					if(R3(containingProcessor.getEXUnit().controlunit.opcode) || R2I1(containingProcessor.getEXUnit().controlunit.opcode)) {
						if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
							conf1 = true;
						}
						if(rs2.equals(containingProcessor.getEXUnit().controlunit.rd)){
								conf1 = true;
						}
					}
					if(R3(containingProcessor.getMAUnit().controlunit.opcode) || R2I1(containingProcessor.getMAUnit().controlunit.opcode)) {
						if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
							conf2   = true;
						}
						if(rs2.equals(containingProcessor.getMAUnit().controlunit.rd)){
							conf2   = true;
						}
					}
					if(R3(containingProcessor.getRWUnit().controlunit.opcode) || R2I1(containingProcessor.getRWUnit().controlunit.opcode)) {
						if(rs1.equals(containingProcessor.getRWUnit().controlunit.rd)){
							conf3   = true;
						}
						if(rs2.equals(containingProcessor.getRWUnit().controlunit.rd)){
							conf3   = true;
						}
					}
					if(containingProcessor.getEXUnit().controlunit.opcode.equals("10110")) {
						if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
							conf1= true;
						}
						if(rs2.equals(containingProcessor.getEXUnit().controlunit.rd)){
							conf1 = true;
						}
					}
					if(containingProcessor.getMAUnit().controlunit.opcode.equals("10110")) {
						if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
							conf2   = true;
						}
						if(rs2.equals(containingProcessor.getMAUnit().controlunit.rd)){
							conf2   = true;
						}
					}
			}
			
			switch(String.valueOf(R2I1(opcode)))
			{
				case "true":
					if(containingProcessor.getEXUnit().controlunit.opcode.equals("00111") || containingProcessor.getEXUnit().controlunit.opcode.equals("00110")) {
						if(rs1.equals("11111")){
							conf1 = true;
						}
					}
					if(R3(containingProcessor.getEXUnit().controlunit.opcode) || R2I1(containingProcessor.getEXUnit().controlunit.opcode)) {
							
						if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
							conf1 = true;
						}
					}
					if(R3(containingProcessor.getMAUnit().controlunit.opcode) || R2I1(containingProcessor.getMAUnit().controlunit.opcode)) {
						if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
							conf2   = true;
						}
					}
					if(R3(containingProcessor.getRWUnit().controlunit.opcode) || R2I1(containingProcessor.getRWUnit().controlunit.opcode)) {
						if(rs1.equals(containingProcessor.getRWUnit().controlunit.rd)){
							conf3   = true;
						}
					}
					if(containingProcessor.getEXUnit().controlunit.opcode.equals("10110")) {
						if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
							conf1 = true;
						}
					}
					if(containingProcessor.getMAUnit().controlunit.opcode.equals("10110")) {
						if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
							conf2   = true;
						}
					}
			}
			
			switch(String.valueOf(R2I3(opcode)))
			{
				case "true":
					if(containingProcessor.getEXUnit().controlunit.opcode.equals("00111") || containingProcessor.getEXUnit().controlunit.opcode.equals("00110")) {
						if(rs1.equals("11111")){
							conf1 = true;
						}
						if(rd.equals("11111")){
							conf1 = true;
						}
					}
					if(R3(containingProcessor.getEXUnit().controlunit.opcode) || R2I1(containingProcessor.getEXUnit().controlunit.opcode)) {
						if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
							conf1 = true;
						}
						if(rd.equals(containingProcessor.getEXUnit().controlunit.rd)){
								conf1 = true;
						}
					}
					if(R3(containingProcessor.getMAUnit().controlunit.opcode) || R2I1(containingProcessor.getMAUnit().controlunit.opcode)) {
						if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
							conf2 = true;
						}
						if(rd.equals(containingProcessor.getMAUnit().controlunit.rd)){
							conf2 = true;
						}
					}
					if(R3(containingProcessor.getRWUnit().controlunit.opcode) || R2I1(containingProcessor.getRWUnit().controlunit.opcode)) {
						if(rs1.equals(containingProcessor.getRWUnit().controlunit.rd)){
							conf3 = true;
						}
						 if(rd.equals(containingProcessor.getRWUnit().controlunit.rd)){
							conf3 = true;
						}
					}
					if(containingProcessor.getEXUnit().controlunit.opcode.equals("10110")) {
						if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
							conf1 = true;
						}
						if(rd.equals(containingProcessor.getEXUnit().controlunit.rd)){
							conf1 = true;
						}
					}
					if(containingProcessor.getMAUnit().controlunit.opcode.equals("10110")) {
						if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
							conf2 = true;
						}
						if(rd.equals(containingProcessor.getMAUnit().controlunit.rd)){
							conf2 = true;
						}
					}
			}
			
			switch(String.valueOf(R2I2(opcode)))
			{
				case "true":
					switch(opcode)
					{
						case "10110":
							if(containingProcessor.getEXUnit().controlunit.opcode.equals("00111") || containingProcessor.getEXUnit().controlunit.opcode.equals("00110")) {
								if(rs1.equals("11111")){
									conf1 = true;
								}
							}
							if(R3(containingProcessor.getEXUnit().controlunit.opcode) || R2I1(containingProcessor.getEXUnit().controlunit.opcode)) {
								if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
									conf1 = true;
								}
							}
							if(R3(containingProcessor.getMAUnit().controlunit.opcode) || R2I1(containingProcessor.getMAUnit().controlunit.opcode)) {
								if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
									conf2   = true;
								}
							}
							if(R3(containingProcessor.getRWUnit().controlunit.opcode) || R2I1(containingProcessor.getRWUnit().controlunit.opcode)) {
								if(rs1.equals(containingProcessor.getRWUnit().controlunit.rd)){
									conf3   = true;
								}
							}
							if(containingProcessor.getEXUnit().controlunit.opcode.equals("10110")) {
								if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
									conf1 = true;
								}
							}
							if(containingProcessor.getMAUnit().controlunit.opcode.equals("10110")) {
								if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
									conf2   = true;
								}
							}
							if(containingProcessor.getEXUnit().controlunit.opcode.equals("10111")) {
								if((Integer.parseInt(rs1,2) + binaryConversion(immx) )== 
												Integer.parseInt((containingProcessor.getEXUnit().controlunit.rd),2) +
												binaryConversion(containingProcessor.getEXUnit().controlunit.Imm) ) {
									conf1 = true;
								}
							}
							if(containingProcessor.getMAUnit().controlunit.opcode.equals("10111")) {
								if((Integer.parseInt(rs1,2) + binaryConversion(immx) )== 
												Integer.parseInt((containingProcessor.getMAUnit().controlunit.rd),2) +
												binaryConversion(containingProcessor.getMAUnit().controlunit.Imm) ) {
									conf2   = true;
								}
							}
					}	
			}
			
			switch(String.valueOf(R2I2(opcode)))
			{
				case "true":
					switch(opcode)
					{
						case "10111":
							if(containingProcessor.getEXUnit().controlunit.opcode.equals("00111") || containingProcessor.getEXUnit().controlunit.opcode.equals("00110")) {
								if(rs1.equals("11111")){
									conf1 = true;
								}
								if(rd.equals("11111")){
									conf1 = true;
								}
							}
							if(R3(containingProcessor.getEXUnit().controlunit.opcode) || R2I1(containingProcessor.getEXUnit().controlunit.opcode)) {
								if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
									conf1 = true;
								}
								if(rd.equals(containingProcessor.getEXUnit().controlunit.rd)){
										conf1 = true;
								}
							}
							if(R3(containingProcessor.getMAUnit().controlunit.opcode) || R2I1(containingProcessor.getMAUnit().controlunit.opcode)) {
								if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
									conf2   = true;
								}
								if(rd.equals(containingProcessor.getMAUnit().controlunit.rd)){
									conf2   = true;
								}
							}
							if(R3(containingProcessor.getRWUnit().controlunit.opcode) || R2I1(containingProcessor.getRWUnit().controlunit.opcode)) {
								if(rs1.equals(containingProcessor.getRWUnit().controlunit.rd)){
									conf3   = true;
								}
								if(rd.equals(containingProcessor.getRWUnit().controlunit.rd)){
									conf3   = true;
								}
							}
							if(containingProcessor.getEXUnit().controlunit.opcode.equals("10110")) {
								if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
									conf1 = true;
								}
								if(rd.equals(containingProcessor.getEXUnit().controlunit.rd)){
									conf1 = true;
								}
							}
							if(containingProcessor.getMAUnit().controlunit.opcode.equals("10110")) {
								if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
									conf2   = true;
								}
								if(rd.equals(containingProcessor.getMAUnit().controlunit.rd)){
									conf2   = true;
								}
							}
					}
			}
			if(!conf1 && !conf2   && !conf3 )  {
				OF_EX_Latch.setInstruction(I);
				OF_EX_Latch.setimmx(binaryConversion(immx));
				OF_EX_Latch.setbranchtarget(binaryConversion(immx) + containingProcessor.getRegisterFile().getProgramCounter()-1);
				OF_EX_Latch.setoperand1(operand1);
				OF_EX_Latch.setoperand2(operand2);
				OF_EX_Latch.setrd(Integer.parseInt(rd,2));
				IF_OF_Latch.setOF_enable(false);
				OF_EX_Latch.setEX_enable(true);
				containingProcessor.getIFUnit().IF_EnableLatch.setIF_enable(true) ;
			}
			else {
				if(conf1 && containingProcessor.getEXUnit().OF_EX_Latch.isEX_enable())
					Statistics.dataHazard++;
				else if(conf2   && containingProcessor.getMAUnit().EX_MA_Latch.isMA_enable() )
					Statistics.dataHazard++;
				else if(conf3   && containingProcessor.getRWUnit().MA_RW_Latch.isRW_enable())
					Statistics.dataHazard++;
				containingProcessor.getIFUnit().IF_EnableLatch.setIF_enable(false);	
			}	
		}	
	}
}
