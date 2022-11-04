package processor.pipeline;
import generic.Statistics;
import processor.Processor;
import java.util.Arrays;

public class Execute {
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	IF_OF_LatchType IF_OF_Latch;
	
	public Execute(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	private static String toBinaryOfSpecificPrecision(int num, int lenOfTargetString) {
		String binary = String.format("%" + lenOfTargetString + "s", Integer.toBinaryString(num)).replace(' ', '0');
		return binary;
	}
	private static int toSignedInteger(String binary) {
		int n = 32 - binary.length();
        char[] sign_ext = new char[n];
        Arrays.fill(sign_ext, binary.charAt(0));
        int signedInteger = (int) Long.parseLong(new String(sign_ext) + binary, 2);
        return signedInteger;
	}
	private void loopAround(int num) {
		for (int i = 0; i < num; i += 1)
			toSignedInteger(toBinaryOfSpecificPrecision(i, 20));
	}

	public void performEX() {

		if (EX_MA_Latch.getIsBusy()) {
			OF_EX_Latch.setIsBusy(true);
		} else {
			OF_EX_Latch.setIsBusy(false);
		}

		int signedInt = toSignedInteger("001");
		String binaryNum = toBinaryOfSpecificPrecision(signedInt, 5);
		loopAround(30);		

		if(OF_EX_Latch.isEX_enable() && EX_MA_Latch.getIsBusy() == false) {
			int offset = 70000;
			Statistics.IncrementInst();
			if(OF_EX_Latch.getIsNOP() == true) {

				EX_MA_Latch.setIsNOP(true);
				EX_MA_Latch.setRD(75000);
			
			} else {
				EX_MA_Latch.setIsNOP(false);

				int ALU_RESULT = 70000;	
				int RS1 = OF_EX_Latch.getRS1();
				int RS2 = OF_EX_Latch.getRS2();
				int RD = OF_EX_Latch.getRD();
				int IMM = OF_EX_Latch.getIMM();

				switch(OF_EX_Latch.getOPcode()) {
					case "00000": {
						ALU_RESULT = RS1 + RS2;
						break;
					}
					case "00001": {
						ALU_RESULT = RS1 + IMM;
						break;
					}
					case "00010": {
						ALU_RESULT = RS1 - RS2;
						break;
					}
					case "00011": {
						ALU_RESULT = RS1 - IMM;
						break;
					}
					case "00100": {
						ALU_RESULT = RS1 * RS2;
						break;
					}
					case "00101": {
						ALU_RESULT = RS1 * IMM;
						break;
					}
					case "00110": {
						ALU_RESULT = RS1 / RS2;
						int temporary = RS1 % RS2;
						containingProcessor.getRegisterFile().setValue(31, temporary);
						break;
					}
					case "00111": {
						ALU_RESULT = RS1 / IMM;
						int temporary = RS1 % IMM;
						containingProcessor.getRegisterFile().setValue(31, temporary);
						break;
					}
					case "01000": {
						ALU_RESULT = RS1 & RS2;
						break;
					}
					case "01001": {
						ALU_RESULT = RS1 & IMM;
						break;
					}
					case "01010": {
						ALU_RESULT = RS1 | RS2;
						break;
					}
					case "01011": {
						ALU_RESULT = RS1 | IMM;
						break;
					}
					case "01100": {
						ALU_RESULT = RS1 ^ RS2;
						break;
					}
					case "01101": {
						ALU_RESULT = RS1 ^ IMM;
						break;
					}
					case "01110": {
						if(RS1 < RS2) ALU_RESULT = 1;
						else ALU_RESULT = 0;
						break;
					}
					case "01111": {
						if(RS1 < IMM) ALU_RESULT = 1;
						else ALU_RESULT = 0;
					}
					case "10000": {
						ALU_RESULT = RS1 << RS2;
						String str = Integer.toBinaryString(RS1);
						while(str.length() != 5) str = "0" + str;
						String x31 = str.substring(5-RS2, 5);
						containingProcessor.getRegisterFile().setValue(31, Integer.parseInt(x31,2));
						break;
					}
					case "10001" : {
						ALU_RESULT = RS1 << IMM;
						String str = Integer.toBinaryString(IMM);
						while(str.length() != 5) str = "0" + str;
						String x31 = str.substring(5-IMM, 5);
						containingProcessor.getRegisterFile().setValue(31, Integer.parseInt(x31,2));
						break;
					}
					case "10010" : {
						ALU_RESULT = RS1 >>> RS2;
						String str = Integer.toBinaryString(RS1);
						while(str.length() != 5) str = "0" + str;
						String x31 = str.substring(0, RS2);
						containingProcessor.getRegisterFile().setValue(31, Integer.parseInt(x31,2));
						break;
					}
					case "10011" : {
						ALU_RESULT = RS1 >>> IMM;
						String str = Integer.toBinaryString(IMM);
						while(str.length() != 5) str = "0" + str;
						String x31 = str.substring(0, IMM);
						containingProcessor.getRegisterFile().setValue(31, Integer.parseInt(x31,2));
						break;
					}
					case "10100" : {
						ALU_RESULT = RS1 >> RS2;
						String str = Integer.toBinaryString(RS1);
						while(str.length() != 5) str = "0" + str;
						String x31 = str.substring(0, RS2);
						containingProcessor.getRegisterFile().setValue(31, Integer.parseInt(x31,2));
						break;
					}
					case "10101" : {
						ALU_RESULT = RS1 >> IMM;
						String str = Integer.toBinaryString(IMM);
						while(str.length() != 5) str = "0" + str;
						String x31 = str.substring(0, IMM);
						containingProcessor.getRegisterFile().setValue(31, Integer.parseInt(x31,2));
						break;
					}
					case "10110"  : {
						ALU_RESULT = RS1 + IMM;
						break;
					}
					case "10111" : {
						ALU_RESULT = containingProcessor.getRegisterFile().getValue(RD) + IMM;
						break;
					}
					case "11000" : {
						offset = containingProcessor.getRegisterFile().getValue(RD) + IMM;
						break;
					}
					case "11001" : {
						if(RS1 == containingProcessor.getRegisterFile().getValue(RD)) offset = IMM;
						break;
					}
					case "11010" : {
						if(RS1 != containingProcessor.getRegisterFile().getValue(RD)) offset = IMM;
						break;
					}
					case "11011" : {
						if(RS1 < containingProcessor.getRegisterFile().getValue(RD)) offset = IMM;
						break;
					}
					case "11100" : {
						if(RS1 > containingProcessor.getRegisterFile().getValue(RD)) offset = IMM;
						break;
					}
					default : break;
				}
				if(offset != 70000) {
					EX_IF_Latch.setIsBranchTaken(true);
					EX_IF_Latch.setOffset(offset - 1);

					IF_EnableLatch.setIF_enable(true);
					IF_OF_Latch.setOF_enable(false);

					OF_EX_Latch.setEX_enable(false);
					OF_EX_Latch.setIMM(0);
					OF_EX_Latch.setRD(0);
					OF_EX_Latch.setRS1(0);
					OF_EX_Latch.setRS2(0);
				}

				// storing in the latch the results obtained in this stage
				EX_MA_Latch.setALUresult(ALU_RESULT);
				EX_MA_Latch.setRS1(RS1);
				EX_MA_Latch.setRS2(RS2);
				EX_MA_Latch.setRD(RD);
				EX_MA_Latch.setIMM(IMM);
				EX_MA_Latch.setOPcode(OF_EX_Latch.getOPcode());				
				EX_MA_Latch.setPC(OF_EX_Latch.getPC()); 

				if (OF_EX_Latch.getOPcode().equals("11101")) {
					OF_EX_Latch.setEX_enable(false); // end instruction
				}
			}
			OF_EX_Latch.setEX_enable(false);
			EX_MA_Latch.setMA_enable(true);	
		}
	}

}
