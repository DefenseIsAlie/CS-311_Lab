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
	Cache cache;
	int currPC;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch, Cache cache)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.cache = cache;
	}
	
	public void performIF()
	{
		if(IF_EnableLatch.isIF_enable()) {
			if(IF_EnableLatch.isBusy == true) {
				return;
			}
			currPC = containingProcessor.getRegisterFile().getProgramCounter();
			boolean isBT = EX_IF_Latch.isBranchTaken;
			if (isBT) {
				currPC += EX_IF_Latch.offset - 1;
				EX_IF_Latch.isBranchTaken = false;
			}
			Simulator.ins_count++;
			MemoryReadEvent EM = new MemoryReadEvent(Clock.getCurrentTime() + this.cache.latency, this, this.cache, currPC);
			Simulator.getEventQueue().addEvent(EM);

			IF_EnableLatch.isBusy = true;
		}
	}

	@Override

	public void handleEvent(Event EVENT) {
		if(IF_OF_Latch.isBusy == true) {
			EVENT.setEventTime(Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(EVENT);
		} else {
			MemoryResponseEvent event = (MemoryResponseEvent) EVENT ; 
			if(EX_IF_Latch.isBranchTaken == false)	{
				IF_OF_Latch.setInstruction(event.getValue());
			} else {
				IF_OF_Latch.setInstruction(0);
			}
			IF_OF_Latch.insPC = this.currPC;
			containingProcessor.getRegisterFile().setProgramCounter(this.currPC + 1);
			IF_OF_Latch.setOF_enable(true);
			IF_EnableLatch.isBusy = false;
		}
	}

}
