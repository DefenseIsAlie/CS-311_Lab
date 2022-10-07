package processor.pipeline;

import java.util.Arrays;

import processor.Processor;
import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand.OperandType;
import generic.Statistics;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	IF_OF_LatchType IF_OF_Latch;
	IF_EnableLatchType IF_EnableLatch;

	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch,
			EX_IF_LatchType eX_IF_Latch, IF_OF_LatchType iF_OF_Latch, IF_EnableLatchType iF_EnableLatch) {
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}

	public void performEX() {
		if (OF_EX_Latch.getIsNOP()) {         // if the instruction is a NOP (possibly inserted due to conflict)
			EX_MA_Latch.setIsNOP(true);       // pass the information to the EX_MA latch by setting isNOP as true
			OF_EX_Latch.setIsNOP(false);      // set isNOP for the OF_EX_Latch as false (default)
			EX_MA_Latch.setInstruction(null); // for NOP instruction packet is null
		} else if (OF_EX_Latch.isEX_enable()) { // if the intruction is not a NOP

			//instruction packet I being retrieved from the OF_EX latch to be executed
			Instruction I = OF_EX_Latch.getInstruction(); 
			
			EX_MA_Latch.setInstruction(I); // saving this packet in the next latch for MA stage
			
			OperationType OPtype = I.getOperationType();
			int OPcode = Arrays.asList(OperationType.values()).indexOf(OPtype);
			int currPC = containingProcessor.getRegisterFile().getProgramCounter() - 1;

			// if the instruction is a branch type (conditional or unconditional) or it is end
			if (OPcode == 24 || OPcode == 25 || OPcode == 26 || OPcode == 27 || OPcode == 28 || OPcode == 29) {
				// since it a branch instruction, disable the corresponding latches (IF, OF, EX) until PC is updated
				IF_EnableLatch.setIF_enable(false);
				IF_OF_Latch.setOF_enable(false);
				OF_EX_Latch.setEX_enable(false);

				// updating the number of wrong branch taken in the statistics object (packet)
				Statistics.setNumberOfBranchTaken(Statistics.getNumberOfBranchTaken() + 2);
			}
			
			int ALUresult = 0;

			if (OPcode % 2 == 0 && OPcode < 21) { //AL instructions without immediate value

				int op1 = containingProcessor.getRegisterFile().getValue(I.getSourceOperand1().getValue()); // first source operand
				int op2 = containingProcessor.getRegisterFile().getValue(I.getSourceOperand2().getValue()); // second source operand

				switch (OPtype) {

					// arithmetic logical instructions
					case add:
						ALUresult = op1 + op2;
						break;
					case sub:
						ALUresult = op1 - op2;
						break;
					case mul:
						ALUresult = op1 * op2;
						break;
					case div:
						ALUresult = op1 / op2;
						int rem = op1 % op2;
						containingProcessor.getRegisterFile().setValue(31, rem); // rem should be set in %x31
						break;
					case and:
						ALUresult = op1 & op2;
						break;
					case or:
						ALUresult = op1 | op2;
						break;
					case xor:
						ALUresult = op1 ^ op2;
						break;

					// bit shifting instructions
					case slt:
						if (op1 < op2)
							ALUresult = 1;
						else
							ALUresult = 0;
						break;
					case sll:
						ALUresult = op1 << op2;
						break;
					case srl:
						ALUresult = op1 >>> op2;
						break;
					case sra:
						ALUresult = op1 >> op2;
						break;
					default:
						break;
				}

			} else if (OPcode < 23) { // AL instructions  with immediate value

				int sourceRegisterCode = I.getSourceOperand1().getValue();
				int op1 = containingProcessor.getRegisterFile().getValue(sourceRegisterCode); // first operand

				int op2 = I.getSourceOperand2().getValue(); // second operand (immediate)

				switch (OPtype) {
					// arithmetic logical instructions with immediate values
					case addi:
						ALUresult = op1 + op2;
						break;
					case subi:
						ALUresult = op1 - op2;
						break;
					case muli:
						ALUresult = op1 * op2;
						break;
					case divi:
						ALUresult = op1 / op2;
						int remainder = op1 % op2;
						containingProcessor.getRegisterFile().setValue(31, remainder);
						break;
					case andi:
						ALUresult = op1 & op2;
						break;
					case ori:
						ALUresult = op1 | op2;
						break;
					case xori:
						ALUresult = op1 ^ op2;
						break;
					
					//bit shifting instructions
					case slti:
						if (op1 < op2)
							ALUresult = 1;
						else
							ALUresult = 0;
						break;
					case slli:
						ALUresult = op1 << op2;
						break;
					case srli:
						ALUresult = op1 >>> op2;
						break;
					case srai:
						ALUresult = op1 >> op2;
						break;
					case load:
						ALUresult = op1 + op2;
						break;
					default:
						break;
				}
			} else if (OPcode == 23) { // store instruction

				int op1 = containingProcessor.getRegisterFile()
						.getValue(I.getDestinationOperand().getValue());
				int op2 = I.getSourceOperand2().getValue();
			
				ALUresult = op1 + op2; // effective address of the memory location where some value has to be stored

			} else if (OPcode == 24) { // jump instruction

				OperandType optype = I.getDestinationOperand().getOperandType();
				int imm = 0;
				if (optype == OperandType.Register) {
					imm = containingProcessor.getRegisterFile()
							.getValue(I.getDestinationOperand().getValue());
				} else {
					imm = I.getDestinationOperand().getValue();
				}

				ALUresult = imm + currPC;
				EX_IF_Latch.setIS_enable(true, ALUresult);
			} else if (OPcode < 29) {  // conditional branches

				int op1 = containingProcessor.getRegisterFile().getValue(I.getSourceOperand1().getValue());
				int op2 = containingProcessor.getRegisterFile().getValue(I.getSourceOperand2().getValue());
				int imm = I.getDestinationOperand().getValue();

				switch (OPtype) {
					case beq:
						if (op1 == op2) { // branch if equal
							ALUresult = imm + currPC;
							EX_IF_Latch.setIS_enable(true, ALUresult);
						}
						break;
					case bne:
						if (op1 != op2) { // branch if not equal
							ALUresult = imm + currPC;
							EX_IF_Latch.setIS_enable(true, ALUresult);
						}
						break;
					case blt:
						if (op1 < op2) { // branch if less than
							ALUresult = imm + currPC;
							EX_IF_Latch.setIS_enable(true, ALUresult);
						}
						break;
					case bgt:
						if (op1 > op2) { // branch if greater than
							ALUresult = imm + currPC;
							EX_IF_Latch.setIS_enable(true, ALUresult);
						}
						break;
					default:
						break;
				}
			}

			// store the ALUresult in the latch for further pipeline stage processing
			EX_MA_Latch.setALU_result(ALUresult);

			// enable MA stage
			EX_MA_Latch.setMA_enable(true);
		}
	}

}
