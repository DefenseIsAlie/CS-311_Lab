package processor.pipeline;

import java.util.Arrays;

import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand.OperandType;
import processor.Processor;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performEX()
	{
		if (OF_EX_Latch.isEX_enable()) {
			Instruction I = OF_EX_Latch.getInstruction();
			EX_MA_Latch.setInstrucion(I);
			OperationType OP = I.getOperationType();

			int opcode = Arrays.asList(OperationType.values()).indexOf(OP);
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter() - 1;
			int ALU_result = 0;


			if(opcode % 2 == 0 && opcode < 21){
				int op1 = containingProcessor.getRegisterFile().getValue(
					I.getSourceOperand1().getValue());
				int op2 = containingProcessor.getRegisterFile().getValue(
					I.getSourceOperand2().getValue());

				switch(OP){
					case add:
						ALU_result = op1 + op2;
						break;
					case sub:
						ALU_result = op1 - op2;
						break;
					case mul:
						ALU_result = op1 * op2;
						break;
					case div:
						ALU_result = op1 / op2;
						int modulo = op1 % op2;
						containingProcessor.getRegisterFile().setValue(31, modulo);
						break;
					case and:
						ALU_result = op1 & op2;
						break;
					case or:
						ALU_result = op1 | op2;
						break;
					case xor:
						ALU_result = op1 ^ op2;
						break;
					case slt:
						if(op1 < op2)
							ALU_result = 1;
						else
							ALU_result = 0;
						break;
					case sll:
						ALU_result = op1 << op2;
						break;
					case srl:
						ALU_result = op1 >>> op2;
						break;
					case sra:
						ALU_result = op1 >> op2;
						break;
					default:
						break;
				}
			}
			else if(opcode < 23){
				int i = I.getSourceOperand1().getValue();
				int op1 = containingProcessor.getRegisterFile().getValue(i);
				int op2 = I.getSourceOperand2().getValue();

				switch(OP){
					case addi:
						ALU_result = op1 + op2;
						break;
					case subi:
						ALU_result = op1 - op2;
						break;
					case muli:
						ALU_result = op1 * op2;
						break;
					case divi:
						ALU_result = op1 / op2;
						int modulo = op1 % op2;
						containingProcessor.getRegisterFile().setValue(31, modulo);
						break;
					case andi:
						ALU_result = op1 & op2;
						break;
					case ori:
						ALU_result = op1 | op2;
						break;
					case xori:
						ALU_result = op1 ^ op2;
						break;
					case slti:
						if(op1 < op2)
							ALU_result = 1;
						else
							ALU_result = 0;
						break;
					case slli:
						ALU_result = op1 << op2;
						break;
					case srli:
						ALU_result = op1 >>> op2;
						break;
					case srai:
						ALU_result = op1 >> op2;
						break;
					case load:
						ALU_result = op1 + op2;
						break;
					default:
						break;
				}
			}
			else if(opcode == 23){
				int op1 = containingProcessor.getRegisterFile().getValue(
				I.getDestinationOperand().getValue());
				int op2 = I.getSourceOperand2().getValue();
				ALU_result = op1 + op2;
			}
			else if(opcode == 24){
				OperandType optype = I.getDestinationOperand().getOperandType();
				int imm = 0;
				if (optype == OperandType.Register){
					imm = containingProcessor.getRegisterFile().getValue(
					I.getDestinationOperand().getValue());
				}
				else{
					imm = I.getDestinationOperand().getValue();
				}
				ALU_result = imm + currentPC;
				EX_IF_Latch.setIsBranchEnable(true);
				EX_IF_Latch.setPC(ALU_result);
			}
			else if(opcode < 29){
				int op1 = containingProcessor.getRegisterFile().getValue(
					I.getSourceOperand1().getValue());
				int op2 = containingProcessor.getRegisterFile().getValue(
					I.getSourceOperand2().getValue());
				int imm = I.getDestinationOperand().getValue();
				switch(OP){
					case beq:
						if(op1 == op2){
							ALU_result = imm + currentPC;
							EX_IF_Latch.setIsBranchEnable(true);
							EX_IF_Latch.setPC(ALU_result);
						}
						break;
					case bne:
						if(op1 != op2){
							ALU_result = imm + currentPC;
							EX_IF_Latch.setIsBranchEnable(true);
							EX_IF_Latch.setPC(ALU_result);
						}

						break;
					case blt:
						if(op1 < op2){
							ALU_result = imm + currentPC;
							EX_IF_Latch.setIsBranchEnable(true);
							EX_IF_Latch.setPC(ALU_result);
						}
						break;
					case bgt:
						if(op1 > op2){
							ALU_result = imm + currentPC;
							EX_IF_Latch.setIsBranchEnable(true);
							EX_IF_Latch.setPC(ALU_result);
						}
						break;
					default:
						break;
				}
			}
			EX_MA_Latch.setALU_result(ALU_result);
		}
	}

}
