package processor.pipeline;

import generic.Simulator;
import processor.Processor;

public class InstructionFetch {
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch,
							IF_OF_LatchType IF_OF_Latch, EX_IF_LatchType EX_IF_Latch) 
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = IF_OF_Latch;
		this.EX_IF_Latch = EX_IF_Latch;
	}
	
	public void performIF() {
		if(IF_EnableLatch.isIF_enable()) { 

			if(EX_IF_Latch.getIS_enable()) { // checking for EX_IF_Latch to be enabled

				// getting the new PC which is saved in EX_IF_Latch after the execute stage of some branch instruction
				int newPC = EX_IF_Latch.getPC(); 

				// the newPC is ready to be read after the execute stage
				// update the program counter and disable the EX_IF_Latch 
				containingProcessor.getRegisterFile().setProgramCounter(newPC);
				EX_IF_Latch.setIS_enable(false); 
			}

			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();

			// fetching the instruction word from the memory address at currentPC location
			int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			
			// after fetching then new instruction, save it in the IF_OF_Latch type and update the PC by 1 (4 bytes)
			IF_OF_Latch.setInstruction(newInstruction);
			containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
		
			/* concern of whether to enable or disable this IF_enable */
			//IF_EnableLatch.setIF_enable(false); // disable the IF stage
			
			IF_OF_Latch.setOF_enable(true);	// enable the OF stage because instruction proceeds to that stage
		}
	}
}
