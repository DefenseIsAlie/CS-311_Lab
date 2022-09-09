package generic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



class helperFunctions {

    public String convert_to_binary_str_len_n(int N, int len) {
        String bin = String.format("%" + len + "s", Integer.toBinaryString(N)).replace(' ', '0');
        return bin;
    }


    public String convert_opd_to_binary_str(Operand OPD, int precision) {
        if (OPD == null) {
            return convert_to_binary_str_len_n(0, precision);
        }
        if (OPD.getOperandType() == Operand.OperandType.Label) {
            // if operand is a label
            return convert_to_binary_str_len_n(ParsedProgram.symtab.get(OPD.getLabelValue()), precision);
        }
        // if operand is a resistor or a immediate
        return convert_to_binary_str_len_n(OPD.getValue(), precision);
    }

}

public class Simulator {

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

	    
        try {
            //1. open the objectProgramFile in binary mode
            FileOutputStream output_binary_file = new FileOutputStream(objectProgramFile);

            //2. write the firstCodeAddress to the file
            byte[] addressCode = ByteBuffer.allocate(4).putInt(ParsedProgram.firstCodeAddress).array();
            output_binary_file.write(addressCode);

            //3. write the data to the file
            for (int value: ParsedProgram.data) {
                byte[] dataValue = ByteBuffer.allocate(4).putInt(value).array();
                output_binary_file.write(dataValue);
            }

            //4. assemble one instruction at a time, and write to the file
            for (Instruction inst: ParsedProgram.code) {
                String inst_to_binary_format = "";
                inst_to_binary_format += OPCODE.get(inst.getOperationType());
                int opCode = Integer.parseInt(inst_to_binary_format, 2);
                int pc = inst.getProgramCounter();

                int by_Instruction_type = 0;

                if (opCode == 24){
                    by_Instruction_type = 1;
                } else if (opCode <= 20 && opCode % 2 == 0 ) {                    
                    by_Instruction_type = 2;
                } else if (opCode == 29) {
                    by_Instruction_type = 3;
                } else if (opCode >= 25 && opCode <= 28) {
                    by_Instruction_type = 4;
                } else {
                    by_Instruction_type = 5;
                }

                switch (by_Instruction_type) {
                    case 1:
                    if (inst.destinationOperand.getOperandType() == Operand.OperandType.Register) {
                        inst_to_binary_format += helper.convert_opd_to_binary_str(inst.getDestinationOperand(), 5);
                        inst_to_binary_format += helper.convert_to_binary_str_len_n(0, 22);
                    }
                    else {						
                        inst_to_binary_format += helper.convert_to_binary_str_len_n(0, 5);
                        int value = Integer.parseInt(helper.convert_opd_to_binary_str(inst.getDestinationOperand(), 5), 2) - pc;
                        String bin = helper.convert_to_binary_str_len_n(value, 22);
                        inst_to_binary_format += bin.substring(bin.length() - 22);
                    }

                    break;

                    case 2:
                    inst_to_binary_format += helper.convert_opd_to_binary_str(inst.getSourceOperand1(), 5);
                    inst_to_binary_format += helper.convert_opd_to_binary_str(inst.getSourceOperand2(), 5);
                    inst_to_binary_format += helper.convert_opd_to_binary_str(inst.getDestinationOperand(), 5);
                    inst_to_binary_format += helper.convert_to_binary_str_len_n(0, 12);
                        
                    break;

                    case 3:
                    inst_to_binary_format += helper.convert_to_binary_str_len_n(0, 27);

                    break;

                    case 4:
                    int value = Integer.parseInt(helper.convert_opd_to_binary_str(inst.getDestinationOperand(), 5), 2) - pc;
                    inst_to_binary_format += helper.convert_opd_to_binary_str(inst.getSourceOperand1(), 5);
                    inst_to_binary_format += helper.convert_opd_to_binary_str(inst.getSourceOperand2(), 5);
                    String bin = helper.convert_to_binary_str_len_n(value, 17);
                    inst_to_binary_format += bin.substring(bin.length() - 17);

                    break;

                    case 5:
                    inst_to_binary_format += helper.convert_opd_to_binary_str(inst.getSourceOperand1(), 5);
                    inst_to_binary_format += helper.convert_opd_to_binary_str(inst.getDestinationOperand(), 5);
                    inst_to_binary_format += helper.convert_opd_to_binary_str(inst.getSourceOperand2(), 17);

                    break;
                
                    default:
                        break;
                }

                
                int instInteger = (int) Long.parseLong(inst_to_binary_format, 2);
                byte[] instBinary = ByteBuffer.allocate(4).putInt(instInteger).array();

                output_binary_file.write(instBinary);
            }
            //5. close the file
            output_binary_file.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
}	
}