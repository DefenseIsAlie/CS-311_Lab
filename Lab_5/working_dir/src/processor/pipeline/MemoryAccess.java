package processor.pipeline;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.MemoryWriteEvent;
import generic.Simulator;
import processor.Clock;
import processor.Processor;

public class MemoryAccess implements Element{
	Processor containingProcessor;
	public EX_MA_LatchType EX_MA_Latch;
	public MA_RW_LatchType MA_RW_Latch;
	co_unit controlunit = new co_unit();
	boolean hasEnded = false;
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	public void performOperations()
	{
		controlunit.opcode="";
		controlunit.rs1="";
		controlunit.rs2="";
		controlunit.rd="";
		controlunit.Imm = "";
	}
	public void setEnableDisable()
	{
		MA_RW_Latch.setRW_enable(true);
		EX_MA_Latch.setMA_enable(false);
	}
	public void performMA() {
		if(EX_MA_Latch.isMA_enable() && !hasEnded) {
			if(EX_MA_Latch.isMA_busy()) {
				return;
			}
			int op2 = EX_MA_Latch.getop2();
			int ALUresult = EX_MA_Latch.getaluRes();
			int loadResult = 0;
			int I = EX_MA_Latch.getInstruction();
			controlunit.setInstruction(I);
			MA_RW_Latch.setInstruction(I);
			if(controlunit.opcode.equals("11101")) {
				hasEnded = true;
			}
			if(controlunit.isSt()){
				Simulator.getEventQueue().addEvent(
						new MemoryWriteEvent(
								Clock.getCurrentTime()+Configuration.mainMemoryLatency,
								this,containingProcessor.getMainMemory(),
								ALUresult,op2));
				EX_MA_Latch.setMA_busy(true);
			}
			else if (controlunit.opcode.equals("10110")){
				Simulator.getEventQueue().addEvent(
						new MemoryReadEvent(
								Clock.getCurrentTime()+Configuration.mainMemoryLatency,
								this,containingProcessor.getMainMemory(),
								ALUresult));
				EX_MA_Latch.setMA_busy(true);
			}
			else{
				MA_RW_Latch.setalures(ALUresult);
				setEnableDisable();
			}
			MA_RW_Latch.setrd(EX_MA_Latch.getrd());
		} else {
			performOperations();
		}
	}
	@Override
	public void handleEvent(Event EVENT) {
		MemoryResponseEvent event=(MemoryResponseEvent) EVENT;
		MA_RW_Latch.setldres(event.getValue());
		EX_MA_Latch.setMA_busy(false);
		MA_RW_Latch.setRW_enable(true);
		EX_MA_Latch.setMA_enable(false);
	}
}
