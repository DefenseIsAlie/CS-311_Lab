package processor.pipeline;

import processor.Processor;
import generic.Instruction;
import generic.Instruction.OperationType;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;

	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch) {
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}

	public void performMA() {
		if (EX_MA_Latch.getIsNOP()) { // if the instruction in NOP
			MA_RW_Latch.setIsNOP(true); // store it in latch to inform the further stages of the pipline
			MA_RW_Latch.setInstruction(null); // set instruction packet as null
			EX_MA_Latch.setIsNOP(false); // after passing the info, set isNOP as false for the current latch (default)
		} else if (EX_MA_Latch.isMA_enable()) {
			
			Instruction I = EX_MA_Latch.getInstruction(); // fetching the instruction packet
			int ALUresult = EX_MA_Latch.getALU_result();  // fetching the ALU result obtained in the execute stage
			OperationType OPtype = I.getOperationType();

			switch (OPtype) { 
				// memory access exits only in case of load and store intructions
				case store:
					int val_store = containingProcessor.getRegisterFile()
							.getValue(I.getSourceOperand1().getValue());
					
					// storing in the value in the address obtained in the execute stage
					containingProcessor.getMainMemory().setWord(ALUresult, val_store);
		
					break;
				case load:
					// load the result from the memory content 
					int load_result = containingProcessor.getMainMemory().getWord(ALUresult);

					//store the loaded result in the MA_RW_latch for writing it into the destination register in RW stage
					MA_RW_Latch.setLoad_result(load_result);
					break;
			default: // when the instruction is neither load nor store
				break;
			}
			
			// if the instruction is end, we are done executing the program, so we disable the IF stage
			if (I.getOperationType().ordinal() == 29) { 
				IF_EnableLatch.setIF_enable(false);
			} 

			// save the ALUresult and intruction packet I in the MA_RW_Latch	
			MA_RW_Latch.setALU_result(ALUresult);
			MA_RW_Latch.setInstruction(I);

			// enable the RW stage
			MA_RW_Latch.setRW_enable(true);
		}
	}

}
