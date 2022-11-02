package processor.pipeline;

import configuration.Configuration;
import generic.*;
import generic.Event.EventType;
import processor.Clock;
import processor.*;
import processor.memorysystem.*;

public class MemoryAccess implements Element{
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	Cache CACHE;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, Cache CACHE)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.CACHE = CACHE;
	}
	
	public void performMA()
	{
		boolean performingCondition = EX_MA_Latch.isMA_enable() && !EX_MA_Latch.getIsBusy();
		
		if(performingCondition) {
			
			if (EX_MA_Latch.getIsNOP()) {
				
				MA_RW_Latch.setIsNOP(true);
				MA_RW_Latch.setRD(75000);

			} else {

				MA_RW_Latch.setIsNOP(false);

				int aluResult = EX_MA_Latch.getALUresult();
				int rs1 = EX_MA_Latch.getRS1();
				int rs2 = EX_MA_Latch.getRS2();
				int rd = EX_MA_Latch.getRD();
				int imm = EX_MA_Latch.getIMM();
				String opcode = EX_MA_Latch.getOPcode();

				MA_RW_Latch.setPC(EX_MA_Latch.getPC());
				MA_RW_Latch.setALUresult(aluResult);
				MA_RW_Latch.setRS1(rs1);
				MA_RW_Latch.setRS2(rs2);
				MA_RW_Latch.setRD(rd);
				MA_RW_Latch.setIMM(imm);
				MA_RW_Latch.setOPcode(opcode);
	
				if(opcode.equals("10110")) { // load instruction
					
					MA_RW_Latch.setIsLoad(true);
					EX_MA_Latch.setIsBusy(true);
					
					MemoryReadEvent MEM_EVENT = new MemoryReadEvent(Clock.getCurrentTime() + this.CACHE.latency, this, this.CACHE, aluResult);
					Simulator.getEventQueue().addEvent(MEM_EVENT);

					EX_MA_Latch.setMA_enable(false);
					return;
				}

				if(opcode.equals("10111")) {  //store instruction
					EX_MA_Latch.setIsBusy(true);
					
					Simulator.storeresp = Clock.getCurrentTime();
					MemoryWriteEvent MEM_WRITE_EVENT = new MemoryWriteEvent(Clock.getCurrentTime() + this.CACHE.latency, this, this.CACHE, aluResult, rs1);
					Simulator.getEventQueue().addEvent(MEM_WRITE_EVENT);
					
					EX_MA_Latch.setMA_enable(false);
					return;
				}
			}

			EX_MA_Latch.setMA_enable(false);
			
			if(EX_MA_Latch.getOPcode().equals("11101") == true ) {
				EX_MA_Latch.setMA_enable(false); // end instruction
			}

			MA_RW_Latch.setRW_enable(true);
		}
	}

	@Override
	public void handleEvent(Event EVENT) {
		if(EVENT.getEventType() == EventType.MemoryResponse) {
			MemoryResponseEvent event = (MemoryResponseEvent) EVENT ; 
			
			MA_RW_Latch.setALUresult(event.getValue());
			MA_RW_Latch.setPC(EX_MA_Latch.getPC());
			EX_MA_Latch.setIsBusy(false);
			MA_RW_Latch.setRW_enable(true);
			EX_MA_Latch.isBusy = false;
		
		}
		else {
			EX_MA_Latch.setIsBusy(false);
		}
	}
}
