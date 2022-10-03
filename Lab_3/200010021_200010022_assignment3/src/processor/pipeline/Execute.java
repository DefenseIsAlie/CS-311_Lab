package processor.pipeline;

import java.util.Arrays;

import processor.Processor;
import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand.OperandType;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch){
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performEX(){
		if(OF_EX_Latch.isEX_enable()){
			Instruction I = OF_EX_Latch.getInstruction(); // instruction packet
			EX_MA_Latch.setInstruction(I);
			OperationType OPtype = I.getOperationType(); // Operation Type object OPtype
			int OPcode = Arrays.asList(OperationType.values()).indexOf(OPtype); // OPcode, encoded value of the operation
			int currPC = containingProcessor.getRegisterFile().getProgramCounter() - 1; // current program counter
			int ALUresult = 0; // ALU result to be obtained in this stage

			if(OPcode % 2 == 0 && OPcode < 21){
				int op1 = containingProcessor.getRegisterFile().getValue(
					I.getSourceOperand1().getValue());
				int op2 = containingProcessor.getRegisterFile().getValue(
					I.getSourceOperand2().getValue());

				switch(OPtype){
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
						int remainder = op1 % op2;
						containingProcessor.getRegisterFile().setValue(31, remainder);
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
					case slt:
						if(op1 < op2)
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
			}
			else if(OPcode < 23){
				int val = I.getSourceOperand1().getValue();
				int op1 = containingProcessor.getRegisterFile().getValue(val);
				int op2 = I.getSourceOperand2().getValue();


				switch(OPtype){
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
					case slti:
						if(op1 < op2)
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
			}
			else if(OPcode == 23){
				int op1 = containingProcessor.getRegisterFile().getValue(
					I.getDestinationOperand().getValue());
				int op2 = I.getSourceOperand2().getValue();
				ALUresult = op1 + op2;
			}
			else if(OPcode == 24){
				OperandType optype = I.getDestinationOperand().getOperandType();
				int imm = 0;
				if (optype == OperandType.Register){
					imm = containingProcessor.getRegisterFile().getValue(
						I.getDestinationOperand().getValue());
				}
				else{
					imm = I.getDestinationOperand().getValue();
				}
				ALUresult = imm + currPC;
				EX_IF_Latch.setIS_enable(true, ALUresult);
			}
			else if(OPcode < 29){
				int op1 = containingProcessor.getRegisterFile().getValue(
					I.getSourceOperand1().getValue());
				int op2 = containingProcessor.getRegisterFile().getValue(
					I.getSourceOperand2().getValue());
				int imm = I.getDestinationOperand().getValue();
				switch(OPtype){
					case beq:
						if(op1 == op2){
							ALUresult = imm + currPC;
							EX_IF_Latch.setIS_enable(true, ALUresult);
						}
						break;
					case bne:
						if(op1 != op2){
							ALUresult = imm + currPC;
							EX_IF_Latch.setIS_enable(true, ALUresult);
						}
						break;
					case blt:
						if(op1 < op2){
							ALUresult = imm + currPC;
							EX_IF_Latch.setIS_enable(true, ALUresult);
						}
						break;
					case bgt:
						if(op1 > op2){
							ALUresult = imm + currPC;
							EX_IF_Latch.setIS_enable(true, ALUresult);
						}
						break;
					default:
						break;
				}
			}
			EX_MA_Latch.setALU_result(ALUresult);
		}

		OF_EX_Latch.setEX_enable(false);
		EX_MA_Latch.setMA_enable(true);
	}

}