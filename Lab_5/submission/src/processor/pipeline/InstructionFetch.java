package processor.pipeline;

import configuration.Configuration;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.Simulator;
import processor.Clock;
import processor.Processor;
import generic.Element;
import generic.Event;

public class InstructionFetch implements Element{
	
	Processor containingProcessor;
	public IF_EnableLatchType IF_EnableLatch;
	public IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	boolean hasEnded = false;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch) {
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void setEnableDisable(Event event) {
		MemoryResponseEvent EVENT = (MemoryResponseEvent) event;
		IF_OF_Latch.setInstruction(EVENT.getValue());
		IF_OF_Latch.setOF_enable(true);
		IF_EnableLatch.setIF_busy(false);
	}
	
	public void performIF() {
			if(IF_EnableLatch.isIF_enable() && !hasEnded) {
				if(IF_EnableLatch.isIF_busy()) {
					return;
				}
				if(EX_IF_Latch.IF_enable){
					if(EX_IF_Latch.isBranchTaken){
						containingProcessor.getRegisterFile().setProgramCounter(EX_IF_Latch.branchTarget);
					}
					EX_IF_Latch.IF_enable=false;
				}
				int currPC = containingProcessor.getRegisterFile().getProgramCounter();
				Simulator.getEventQueue().addEvent(
							new MemoryReadEvent(
									Clock.getCurrentTime()+Configuration.mainMemoryLatency,
									this,containingProcessor.getMainMemory(),
									containingProcessor.getRegisterFile().getProgramCounter()));
					IF_EnableLatch.setIF_busy(true);
					containingProcessor.getRegisterFile().setProgramCounter(currPC + 1);
			}
	}

	@Override
	public void handleEvent(Event event) {
		if(IF_OF_Latch.OF_busy) {
			event.setEventTime(Clock.getCurrentTime()+1);
			Simulator.getEventQueue().addEvent(event);
		}
		else {
			setEnableDisable(event);
		}
		
	}
}
