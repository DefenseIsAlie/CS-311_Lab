package processor.pipeline;

import processor.Processor;
import java.util.Arrays;

public class OperandFetch {
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch,IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}

// helper functions

	public static char bitComplement(char c) {
		return (c == '0') ? '1' : '0';
	}
	public static String twosComplement(String bin) {
		String TWO = "", ONE = ""; // ones and twos complement respectively
		// taking ones complement
		for (int i = 0; i < bin.length(); i++){
            ONE += bitComplement(bin.charAt(i));
        }
		// Building the string for twos complement
        StringBuilder B = new StringBuilder(ONE);
        boolean f = false;
        for (int i = ONE.length() - 1; i > 0; i--){
            if (ONE.charAt(i) == '1'){
                B.setCharAt(i, '0');
            }
            else{
                B.setCharAt(i, '1');
                f = true;
                break;
            }
        }
        if (!f){
            B.append("1", 0, 7);
        }
        TWO = B.toString();
        return TWO;
	}
	private static String toBinaryOfSpecificPrecision(int num, int targetLength) {
		String binary = String.format("%" + targetLength + "s", Integer.toBinaryString(num)).replace(' ', '0');
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
	
	public void performOF() {

		if (OF_EX_Latch.getIsBusy()) {
			IF_OF_Latch.setIsBusy(true); // stall the pipeline
			return;
		} else {
			IF_OF_Latch.setIsBusy(false);
		}

		if(IF_OF_Latch.isOF_enable()) {
			
			String I_STR = Integer.toBinaryString(IF_OF_Latch.getInstruction());
			
			if(IF_OF_Latch.getInstruction() < 0) {
				while(I_STR.length() < 32) {
					I_STR = "1" + I_STR;
				}
			} else {
				while(I_STR.length() < 32) {
					I_STR = "0" + I_STR;
				}
			}
			
			int OPCODE;
			int rd;
			int rs1;
			int rs2;
			int IMM;
			int rs1Address,rs2Address;

			String op = I_STR.substring(0, 5);
			OPCODE = Integer.parseInt(op,2);

			rs1 = 70000;
			rs2 = 70000;
			rd  = 70000;
			IMM = 70000;

			rs1Address = 45;
			rs2Address = 45;

			if (OPCODE == 0) {
				rs1Address = Integer.parseInt(I_STR.substring(5, 10),2);
				rs2Address = Integer.parseInt(I_STR.substring(10, 15),2);
				rs1 = containingProcessor.getRegisterFile().getValue(rs1Address);
				rs2 = containingProcessor.getRegisterFile().getValue(rs2Address);
				rd = Integer.parseInt(I_STR.substring(15, 20),2);
				IMM = 70000;
			} else if (0 < OPCODE && OPCODE < 22) {
				if(OPCODE % 2 == 0) {
					rs1Address = Integer.parseInt(I_STR.substring(5, 10),2);
					rs2Address = Integer.parseInt(I_STR.substring(10, 15),2);
					rs1 = containingProcessor.getRegisterFile().getValue(rs1Address);
					rs2 = containingProcessor.getRegisterFile().getValue(rs2Address);
					rd = Integer.parseInt(I_STR.substring(15, 20),2);
					IMM = 70000;
				} else {
					rs1Address = Integer.parseInt(I_STR.substring(5, 10),2);
					rs1 = containingProcessor.getRegisterFile().getValue(rs1Address);
					rs2 = 70000;
					rd = Integer.parseInt(I_STR.substring(10, 15),2);
					IMM = Integer.parseInt(I_STR.substring(15, 32),2);
				}
			} else {
				if(OPCODE == 24) {
					rs1 = 70000;
					rs2 = 70000;
					rd = Integer.parseInt(I_STR.substring(5, 10),2);
					IMM = Integer.parseInt(I_STR.substring(10, 32),2);
					if(I_STR.substring(10, 32).charAt(0) == '1') {
						IMM = IMM - 4194304;
					}
				}
				else if(OPCODE != 29) {
					rs1Address = Integer.parseInt(I_STR.substring(5, 10),2);
					rs1 = containingProcessor.getRegisterFile().getValue(rs1Address);
					rs2 = 70000;
					rd = Integer.parseInt(I_STR.substring(10, 15),2);
					IMM = Integer.parseInt(I_STR.substring(15, 32),2);
					if(I_STR.substring(15, 32).charAt(0) == '1') {
						IMM = IMM - 131072;
					}
				}
				else {
					rs1 = 70000;
					rs2 = 70000;
					rd = 70000;
					IMM = 70000;
				}
			}

			OF_EX_Latch.setIsNOP(false);
			OF_EX_Latch.setOPcode(op);
			OF_EX_Latch.setRS1(rs1);
			OF_EX_Latch.setRS2(rs2);
			OF_EX_Latch.setRD(rd);
			OF_EX_Latch.setIMM(IMM);
			OF_EX_Latch.setPC(IF_OF_Latch.getPC());
			OF_EX_Latch.setEX_enable(true);

			IF_EnableLatch.setIF_enable(true);
		
			if(OPCODE == 29) { // end instruction
				IF_OF_Latch.setOF_enable(false);
				IF_EnableLatch.setIF_enable(false);
			}

			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}

}
