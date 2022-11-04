package processor.pipeline;

import configuration.Configuration;
import generic.*;
import processor.Clock;
import processor.Processor;
import processor.*;	
import processor.memorysystem.*;	

public class InstructionFetch implements Element{
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	Cache CACHE;
	int currPC;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch, Cache CACHE)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.CACHE = CACHE;
	}
	
	public void performIF()
	{
		if(IF_EnableLatch.isIF_enable()) {

			if(IF_EnableLatch.getIsBusy() == true) {
				return;
			}

			currPC = containingProcessor.getRegisterFile().getProgramCounter();
			boolean isBT = EX_IF_Latch.getIsBranchTaken();
		
			if (isBT == true) {
				currPC += EX_IF_Latch.offset - 1;
				EX_IF_Latch.setIsBranchTaken(false);
			}

			// increment number of instructions
			Simulator.setInstructionCount(Simulator.getInstructionCount() + 1);

			MemoryReadEvent EM = new MemoryReadEvent(Clock.getCurrentTime() + this.CACHE.latency, this, this.CACHE, currPC);
			Simulator.getEventQueue().addEvent(EM);

			IF_EnableLatch.setIsBusy(true);
		}
	}

	@Override
	public void handleEvent(Event EVENT) {

		if(IF_OF_Latch.getIsBusy() == true) {
			EVENT.setEventTime(Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(EVENT);
		} else {
			MemoryResponseEvent MEMORY_EVENT = (MemoryResponseEvent) EVENT;
			
			if (EX_IF_Latch.getIsBranchTaken()) {
				IF_OF_Latch.setInstruction(0);
			} else {
				IF_OF_Latch.setInstruction(MEMORY_EVENT.getValue());
			}  

			IF_OF_Latch.setPC(this.currPC);
			containingProcessor.getRegisterFile().setProgramCounter(this.currPC + 1);
			IF_OF_Latch.setOF_enable(true);
			IF_EnableLatch.setIsBusy(false);
		}
	}

}
