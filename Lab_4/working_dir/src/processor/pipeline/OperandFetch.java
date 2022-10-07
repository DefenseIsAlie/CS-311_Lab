package processor.pipeline;

import processor.Processor;
import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand;
import generic.Statistics;
import generic.Operand.OperandType;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch) {
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}

	public static char bitComplement(char c){
		if (c == '0') return '1';
		else return '0';
    }

	public static String twosComplement(String bin){
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
	
	public static boolean checkConflict(Instruction I, int r1, int r2) {
		// r1 and r2 are source registers (producers)
		int OPcodeDecimal = I != null && I.getOperationType() != null ? I.getOperationType().ordinal() : 100000;
	
		if (OPcodeDecimal <= 23) { 
			int rd = I != null ? I.getDestinationOperand().getValue() : -1; // fetching the destination register (consumer)
			
			if (r1 == rd || r2 == rd) return true; // There is conflict (RAW)
			else return false;

		} else {
			return false;
		} 
	}
	
	// denominator being zero denominator should not be zero for division
	public boolean checkConflictWithDivision(int r1, int r2) { 
		Instruction I_EXstage = OF_EX_Latch.getInstruction();
		Instruction I_MAstage = EX_MA_Latch.getInstruction();
		Instruction I_RWstage = MA_RW_Latch.getInstruction();
		if (r1 == 31 || r2 == 31) {
			//getting the opcode decimal for each intruction stage (EX, MA, RW)
			int I_EXdecimal = I_EXstage != null && I_EXstage.getOperationType() != null ? I_EXstage.getOperationType().ordinal() : 100000;
			int I_MAdecimal = I_MAstage != null && I_MAstage.getOperationType() != null ? I_MAstage.getOperationType().ordinal() : 100000;
			int I_RWdecimal = I_RWstage != null && I_RWstage.getOperationType() != null ? I_RWstage.getOperationType().ordinal() : 100000;
			
			// operation being either div or divi (immediate)
			boolean cnd = I_EXdecimal == 6 || I_EXdecimal == 7 || I_MAdecimal == 6 || I_MAdecimal == 7 || I_RWdecimal == 6 || I_RWdecimal == 7;
			if (cnd) return true;
			else return false;
		} else return false;
	}
	
	public void conflictBubblePCModify () {
		// disable IF stage and pass NOP instruction to the EX stage
		IF_EnableLatch.setIF_enable(false);
		OF_EX_Latch.setIsNOP(true);
	}
 	
	public void performOF() {
		if (IF_OF_Latch.isOF_enable()) {
			Statistics.setNumberOfOFInstructions(Statistics.getNumberOfOFInstructions() + 1);
			
			OperationType[] operationType = OperationType.values();
			String instruction = Integer.toBinaryString(IF_OF_Latch.getInstruction());
			while (instruction.length() != 32) {
				instruction = "0" + instruction;
			}

			String OPcode = instruction.substring(0, 5); 
			int type_operation = Integer.parseInt(OPcode, 2);
			OperationType OP = operationType[type_operation]; 
			
			// checking if the opcode corresponds to a branch instruction
			if (OP.ordinal() == 24 || OP.ordinal() == 25 || OP.ordinal() == 26 || OP.ordinal() == 27 || OP.ordinal() == 28 ) {
				IF_EnableLatch.setIF_enable(false); // pause the IF stage
			}
		
			boolean conflict_inst = false;

			Instruction I_EXstage = OF_EX_Latch.getInstruction();
			Instruction I_MAstage = EX_MA_Latch.getInstruction();
			Instruction I_RWstage = MA_RW_Latch.getInstruction();
			
			Instruction I = new Instruction(); // new instruction packet to be stored in the subsequent latch

			switch (OP) {
				// Arithmetic and logical instructions without immediate parameter
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

					Operand rs1 = new Operand(); // source register 1
					rs1.setOperandType(OperandType.Register);
					int registerNo = Integer.parseInt(instruction.substring(5, 10), 2);
					rs1.setValue(registerNo);

					Operand rs2 = new Operand(); // source register 2
					rs2.setOperandType(OperandType.Register);
					int registerNo2 = Integer.parseInt(instruction.substring(10, 15), 2);
					rs2.setValue(registerNo2);

					// Checking conflicts
					if (checkConflict(I_EXstage, registerNo, registerNo2))
						conflict_inst = true;
					if (checkConflict(I_MAstage, registerNo, registerNo2))
						conflict_inst = true;
					if (checkConflict(I_RWstage, registerNo, registerNo2))
						conflict_inst = true;
					if (checkConflictWithDivision(registerNo, registerNo2)) {
						conflict_inst = true;
					}

					if (conflict_inst) { 
						// if any conflict exists we insert nop instruction (bubble) and pause the IFstage
						this.conflictBubblePCModify();
						break;
					}

					Operand rd = new Operand(); // destination register
					rd.setOperandType(OperandType.Register);
					registerNo = Integer.parseInt(instruction.substring(15, 20), 2);
					rd.setValue(registerNo);

					I.setOperationType(operationType[type_operation]);
					I.setSourceOperand1(rs1);
					I.setSourceOperand2(rs2);
					I.setDestinationOperand(rd);
					break;

			case end:
				I.setOperationType(operationType[type_operation]);
				IF_EnableLatch.setIF_enable(false);
				break;

			case jmp:
				Operand op = new Operand();
				String imm = instruction.substring(10, 32);
				int imm_val = Integer.parseInt(imm, 2);

				if (imm.charAt(0) == '1') {
					imm = twosComplement(imm);
					imm_val = Integer.parseInt(imm, 2) * -1;
				}
				if (imm_val != 0) {
					op.setOperandType(OperandType.Immediate);
					op.setValue(imm_val);
				} else {
					registerNo = Integer.parseInt(instruction.substring(5, 10), 2);
					op.setOperandType(OperandType.Register);
					op.setValue(registerNo);
				}

				I.setOperationType(operationType[type_operation]);
				I.setDestinationOperand(op);
				break;

			// conditional branches
			case beq:
			case bne:
			case blt:
			case bgt:
				rs1 = new Operand();
				rs1.setOperandType(OperandType.Register);
				registerNo = Integer.parseInt(instruction.substring(5, 10), 2);
				rs1.setValue(registerNo);
				
				// destination register
				rs2 = new Operand();
				rs2.setOperandType(OperandType.Register);
				registerNo2 = Integer.parseInt(instruction.substring(10, 15), 2);
				rs2.setValue(registerNo2);
				
				if (checkConflict(I_EXstage, registerNo, registerNo2))
					conflict_inst = true;
				if (checkConflict(I_MAstage, registerNo, registerNo2))
					conflict_inst = true;
				if (checkConflict(I_RWstage, registerNo, registerNo2))
					conflict_inst = true;
				if (checkConflictWithDivision(registerNo, registerNo2)) {
					conflict_inst = true;
				}

				if (conflict_inst) {
					// if any conflict exists we insert nop instruction (bubble) and pause the IFstage
					this.conflictBubblePCModify();
					break;
				}
				
				// Immediate value
				rd = new Operand();
				rd.setOperandType(OperandType.Immediate);
				imm = instruction.substring(15, 32);
				imm_val = Integer.parseInt(imm, 2);
				if (imm.charAt(0) == '1') {
					imm = twosComplement(imm);
					imm_val = Integer.parseInt(imm, 2) * -1;
				}
				rd.setValue(imm_val);
				
				I.setOperationType(operationType[type_operation]);
				I.setSourceOperand1(rs1);
				I.setSourceOperand2(rs2);
				I.setDestinationOperand(rd);
				break;

			default:
				rs1 = new Operand();
				rs1.setOperandType(OperandType.Register);
				registerNo = Integer.parseInt(instruction.substring(5, 10), 2);
				rs1.setValue(registerNo);

				if (checkConflict(I_EXstage, registerNo, registerNo)) {
					conflict_inst = true;
				}	
				if (checkConflict(I_MAstage, registerNo, registerNo)) {
					conflict_inst = true;
				}
				if (checkConflict(I_RWstage, registerNo, registerNo)) {
					conflict_inst = true;
				}
				if (checkConflictWithDivision(registerNo, registerNo)) {
					conflict_inst = true;
				}
					
				if (conflict_inst) {
					// if any conflict exists we insert nop instruction (bubble) and pause the IFstage
					this.conflictBubblePCModify();
					break;
				}

				// Destination register
				rd = new Operand();
				rd.setOperandType(OperandType.Register);
				registerNo = Integer.parseInt(instruction.substring(10, 15), 2);
				rd.setValue(registerNo);

				// Immediate values
				rs2 = new Operand();
				rs2.setOperandType(OperandType.Immediate);
				imm = instruction.substring(15, 32);
				imm_val = Integer.parseInt(imm, 2);
				if (imm.charAt(0) == '1') {
					imm = twosComplement(imm);
					imm_val = Integer.parseInt(imm, 2) * -1;
				}
				rs2.setValue(imm_val);


				I.setOperationType(operationType[type_operation]);
				I.setSourceOperand1(rs1);
				I.setSourceOperand2(rs2);
				I.setDestinationOperand(rd);
				break;
			}
			// saving intruction packet in the OF_EX_Latch for the execute stage
			OF_EX_Latch.setInstruction(I);
			OF_EX_Latch.setEX_enable(true);
		}
	}

}
