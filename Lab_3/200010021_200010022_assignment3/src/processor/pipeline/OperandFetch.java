package processor.pipeline;

import processor.Processor;
import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand;
import generic.Operand.OperandType;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
		
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch){
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}

	public static char bitComplement(char c){
		if (c == '0') return '1';
		else return '0';
    }
	
	public static String twosComplement(String Binary){
        String TWO = "", ONE = ""; // ones and twos complement respectively
        for (int i = 0; i < Binary.length(); i++){
            ONE += bitComplement(Binary.charAt(i));
        }
        StringBuilder B = new StringBuilder(ONE);
        boolean bool = false;
        for (int i = ONE.length() - 1; i > 0; i--){
            if (ONE.charAt(i) == '1'){
                B.setCharAt(i, '0');
            }
            else{
                B.setCharAt(i, '1');
                bool = true;
                break;
            }
        }
        if (!bool){
            B.append("1", 0, 7);
        }
        TWO = B.toString();
        return TWO;
    }
	
	public void performOF(){
		if(IF_OF_Latch.isOF_enable()){
			OperationType[] operationSet = OperationType.values();
			String I = Integer.toBinaryString(IF_OF_Latch.getInstruction());
			while(I.length()!=32){
				I = "0" + I;
			}
			String OPcode = I.substring(0, 5);
			int type_operation = Integer.parseInt(OPcode, 2);
			OperationType OP = operationSet[type_operation];

			Instruction newInstruction = new Instruction();
			switch(OP){
				// Cases without immediate values and destination register being rd (simple Arithmetic and Logical Instructions)
				case add:
				case sub:
				case mul:
				case div:
				case and:
				case or:
				case xor:
				case slt:
				case sll:
				case srl:
				case sra:
					Operand rs1 = new Operand(); //source register 1
					rs1.setOperandType(OperandType.Register);
					int regNo = Integer.parseInt(I.substring(5, 10), 2);
					rs1.setValue(regNo);

					Operand rs2 = new Operand(); // source register 2
					rs2.setOperandType(OperandType.Register);
					regNo = Integer.parseInt(I.substring(10, 15), 2);
					rs2.setValue(regNo);

					Operand rd = new Operand(); // destination register
					rd.setOperandType(OperandType.Register);
					regNo = Integer.parseInt(I.substring(15, 20), 2);
					rd.setValue(regNo);

					// setting all the necessary objects in the instruction packet
					newInstruction.setOperationType(operationSet[type_operation]);
					newInstruction.setSourceOperand1(rs1);
					newInstruction.setSourceOperand2(rs2);
					newInstruction.setDestinationOperand(rd);
					break;

				case end:
					newInstruction.setOperationType(operationSet[type_operation]);
					break;

				// in case of unconditional branch
				case jmp:
					Operand op = new Operand();
					String imm = I.substring(10, 32);
					int imm_val = Integer.parseInt(imm, 2);
					if (imm.charAt(0) == '1'){
						imm = twosComplement(imm);
						imm_val = Integer.parseInt(imm, 2) * -1;
					}
					if (imm_val != 0){
						op.setOperandType(OperandType.Immediate);
						op.setValue(imm_val);
					}
					else{
						regNo = Integer.parseInt(I.substring(5, 10), 2);
						op.setOperandType(OperandType.Register);
						op.setValue(regNo);
					}
					newInstruction.setOperationType(operationSet[type_operation]);
					newInstruction.setDestinationOperand(op);
					break;
			
				// in cases of condition branches
				case beq:
				case bne:
				case blt:
				case bgt:
					rs1 = new Operand();
					rs1.setOperandType(OperandType.Register);
					regNo = Integer.parseInt(I.substring(5, 10), 2);
					rs1.setValue(regNo);

					// destination register
					rs2 = new Operand();
					rs2.setOperandType(OperandType.Register);
					regNo = Integer.parseInt(I.substring(10, 15), 2);
					rs2.setValue(regNo);

					// Immediate value
					rd = new Operand();
					rd.setOperandType(OperandType.Immediate);
					imm = I.substring(15, 32);
					imm_val = Integer.parseInt(imm, 2);
					if (imm.charAt(0) == '1'){
						imm = twosComplement(imm);
						imm_val = Integer.parseInt(imm, 2) * -1;
					}
					rd.setValue(imm_val);

					// storing operands and operation type in the instruction packet
					newInstruction.setOperationType(operationSet[type_operation]);
					newInstruction.setSourceOperand1(rs1);
					newInstruction.setSourceOperand2(rs2);
					newInstruction.setDestinationOperand(rd);
					break;
				
				// case where immediate is used in an instruction without branches
				default:
					// Source register 1
					rs1 = new Operand();
					rs1.setOperandType(OperandType.Register);
					regNo = Integer.parseInt(I.substring(5, 10), 2);
					rs1.setValue(regNo);

					// Destination register
					rd = new Operand();
					rd.setOperandType(OperandType.Register);
					regNo = Integer.parseInt(I.substring(10, 15), 2);
					rd.setValue(regNo);

					// Immediate values
					rs2 = new Operand();
					rs2.setOperandType(OperandType.Immediate);
					imm = I.substring(15, 32);
					imm_val = Integer.parseInt(imm, 2);
					if (imm.charAt(0) == '1'){
						imm = twosComplement(imm);
						imm_val = Integer.parseInt(imm, 2) * -1;
					}
					rs2.setValue(imm_val);

					// stroring operands and operation type in the instruction packet
					newInstruction.setOperationType(operationSet[type_operation]);
					newInstruction.setSourceOperand1(rs1);
					newInstruction.setSourceOperand2(rs2);
					newInstruction.setDestinationOperand(rd);
					break;
			}

			OF_EX_Latch.setInstruction(newInstruction); // storing the instruction packet in pipeline register
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}

}