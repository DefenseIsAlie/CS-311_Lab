package generic;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import generic.Operand.OperandType;

public class Simulator {
	
	//Helper Function Class
	public static class helperFunctions {

		public String toBinaryOfSpecificPrecision(int N, int len) {
			String bin = String.format("%" + len + "s", Integer.toBinaryString(N)).replace(' ', '0');
			return bin;
		}

		public int toSignedInteger(String bin) {
			int leftOver = 32 - bin.length();
			char[] signedPrefix = new char[leftOver];
			//fill 0 in the prefix if MSB in bin is 0 ELSE fill 1 in the prefix if MSB in bin is 1
			Arrays.fill(signedPrefix, bin.charAt(0));
			int resultantSignInt = (int) Long.parseLong(new String(signedPrefix)+ bin, 2);
			return resultantSignInt;
		}

		public String toBinaryString(int N) {
			ArrayList<Integer> binBits = new ArrayList<Integer>();
			while (N > 0) {
				binBits.add(N % 2);
				N /= 2;
			}
			StringBuilder binForm = new StringBuilder();
			//Iterating in reverse order will give us the correct binary form
			for (int i = binBits.size() - 1; i >= 0; i--) {
				binForm.append(binBits.get(i));
			}
			return " " + binForm.toString();
		}

		public String convert(Operand OPD, int precision) {
			if (OPD == null) {
				return toBinaryOfSpecificPrecision(0, precision);
			}
			if (OPD.getOperandType() == Operand.OperandType.Label) {
				// if operand is a label
				return toBinaryOfSpecificPrecision(ParsedProgram.symtab.get(OPD.getLabelValue()), precision);
			}
			// if operand is a resistor or a immediate
			return toBinaryOfSpecificPrecision(OPD.getValue(), precision);
		}

	}

	static FileInputStream inputcodeStream = null;
	
	public static void setupSimulation(String assemblyProgramFile) {	
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();
	}
	
	public static void assemble(String objectProgramFile) {
        // HashMap for storing OPCODE of the type of operation in a 5-bit binary form
        Map<Instruction.OperationType, String> OPCODE = new HashMap<Instruction.OperationType, String>() {
            {
                put(Instruction.OperationType.add , "00000");
                put(Instruction.OperationType.addi, "00001");
                put(Instruction.OperationType.sub , "00010");
                put(Instruction.OperationType.subi, "00011");
                put(Instruction.OperationType.mul , "00100");
                put(Instruction.OperationType.muli, "00101");
                put(Instruction.OperationType.div , "00110");
                put(Instruction.OperationType.divi, "00111");
                put(Instruction.OperationType.and , "01000");
                put(Instruction.OperationType.andi, "01001");
                put(Instruction.OperationType.or  , "01010");
                put(Instruction.OperationType.ori , "01011");
                put(Instruction.OperationType.xor , "01100");
                put(Instruction.OperationType.xori, "01101");
                put(Instruction.OperationType.slt , "01110");
                put(Instruction.OperationType.slti, "01111");
                put(Instruction.OperationType.sll , "10000");
                put(Instruction.OperationType.slli, "10001");
                put(Instruction.OperationType.srl , "10010");
                put(Instruction.OperationType.srli, "10011");
                put(Instruction.OperationType.sra , "10100");
                put(Instruction.OperationType.srai, "10101");
                put(Instruction.OperationType.load, "10110");
                put(Instruction.OperationType.end , "11101");
                put(Instruction.OperationType.beq , "11001");
                put(Instruction.OperationType.jmp , "11000");
                put(Instruction.OperationType.bne , "11010");
                put(Instruction.OperationType.blt , "11011");
                put(Instruction.OperationType.bgt , "11100");
            } 
        };

	    helperFunctions helper = new helperFunctions();

	    FileOutputStream file;
        try {
            //1. open the objectProgramFile in binary mode
            file = new FileOutputStream(objectProgramFile);
            BufferedOutputStream bfile = new BufferedOutputStream(file);

            //2. write the firstCodeAddress to the file
            byte[] addressCode = ByteBuffer.allocate(4).putInt(ParsedProgram.firstCodeAddress).array();
            bfile.write(addressCode);

            //3. write the data to the file
            for (int value: ParsedProgram.data) {
                byte[] dataValue = ByteBuffer.allocate(4).putInt(value).array();
                bfile.write(dataValue);
            }

            //4. assemble one instruction at a time, and write to the file
            for (Instruction inst: ParsedProgram.code) {
                String binaryRep = "";
                binaryRep += OPCODE.get(inst.getOperationType());
                int opCode = Integer.parseInt(binaryRep, 2);
                int pc = inst.getProgramCounter();
                
                if (opCode <= 20 && opCode % 2 == 0) {
                    // R3 Type
                    binaryRep += helper.convert(inst.getSourceOperand1(), 5);
                    binaryRep += helper.convert(inst.getSourceOperand2(), 5);
                    binaryRep += helper.convert(inst.getDestinationOperand(), 5);
                    binaryRep += helper.toBinaryOfSpecificPrecision(0, 12);
                }
                else if (opCode == 24) {
                    // RI Type
                    if (inst.destinationOperand.getOperandType() == Operand.OperandType.Register) {
                        binaryRep += helper.convert(inst.getDestinationOperand(), 5);
                        binaryRep += helper.toBinaryOfSpecificPrecision(0, 22);
                    }
                    else {						
                        binaryRep += helper.toBinaryOfSpecificPrecision(0, 5);
                        int value = Integer.parseInt(helper.convert(inst.getDestinationOperand(), 5), 2) - pc;
                        String bin = helper.toBinaryOfSpecificPrecision(value, 22);
                        binaryRep += bin.substring(bin.length() - 22);
                    }
                }
                else if (opCode == 29) {
                    binaryRep += helper.toBinaryOfSpecificPrecision(0, 27);
                }
                else {
                    // R2I Type
                    if (opCode >= 25 && opCode <= 28) {
                        int value = Integer.parseInt(helper.convert(inst.getDestinationOperand(), 5), 2) - pc;
                        binaryRep += helper.convert(inst.getSourceOperand1(), 5);
                        binaryRep += helper.convert(inst.getSourceOperand2(), 5);
                        String bin = helper.toBinaryOfSpecificPrecision(value, 17);
                        binaryRep += bin.substring(bin.length() - 17);
                    }
                    else {						
                        binaryRep += helper.convert(inst.getSourceOperand1(), 5);
                        binaryRep += helper.convert(inst.getDestinationOperand(), 5);
                        binaryRep += helper.convert(inst.getSourceOperand2(), 17);
                    }
                }
                int instInteger = (int) Long.parseLong(binaryRep, 2);
                byte[] instBinary = ByteBuffer.allocate(4).putInt(instInteger).array();
                bfile.write(instBinary);
            }
            //5. close the file
            bfile.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
}	
}