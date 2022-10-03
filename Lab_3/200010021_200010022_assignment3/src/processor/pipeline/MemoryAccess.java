package processor.pipeline;

import processor.Processor;
import generic.Instruction;
import generic.Instruction.OperationType;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch){
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA(){
		if(EX_MA_Latch.isMA_enable()){
			Instruction I = EX_MA_Latch.getInstruction();
			int ALUresult = EX_MA_Latch.getALU_result();
			
			OperationType OPtype = I.getOperationType();
			switch(OPtype){
				case store:
					int valueToStore = containingProcessor.getRegisterFile().getValue(
							I.getSourceOperand1().getValue()); // value to be stored
					// ALUresult => address at which this value is to be stored 
					containingProcessor.getMainMemory().setWord(ALUresult, valueToStore);
					break;

				case load:
					int loadResult = containingProcessor.getMainMemory().getWord(ALUresult);
					MA_RW_Latch.setLoad_result(loadResult); // storing the load result in next pipeline register for the next cycle
					break;
					
				default:
					break;
			}

			// saving required information about an instruction in the pipeline registers
			MA_RW_Latch.setALU_result(ALUresult);
			MA_RW_Latch.setInstruction(I);

			MA_RW_Latch.setRW_enable(true);
			EX_MA_Latch.setMA_enable(false);
		}
	}

}