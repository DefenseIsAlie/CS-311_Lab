package processor.pipeline;

import generic.Simulator;
import generic.Statistics;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	co_unit controlunit = new co_unit();
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void setEnableDisable()
	{
		MA_RW_Latch.setRW_enable(false);
		IF_EnableLatch.setIF_enable(true);
	}
	
	public void doOperations()
	{
		IF_EnableLatch.setIF_enable(true);
		controlunit.opcode="";
		controlunit.rs1="";
		controlunit.rs2="";
		controlunit.rd="";
		controlunit.Imm = "";
	}
	
	public void performRW() {
		if(MA_RW_Latch.isRW_enable()) {
			Statistics.setNumberOfInstructions(Statistics.noOfInstructions + 1);
			int I = MA_RW_Latch.getInstruction();
			controlunit.setInstruction(I);
			if(controlunit.opcode.equals("11101")){
				Simulator.setSimulationComplete(true);
				MA_RW_Latch.setRW_enable(false);
				IF_EnableLatch.setIF_enable(false);
			} else {
				int finalResult;
				switch(controlunit.opcode) {
					case "10110":
						finalResult = MA_RW_Latch.getldres();
						break;
					default:
						finalResult = MA_RW_Latch.getalures();
						break;
				}
				int rd = MA_RW_Latch.getrd();
				if(controlunit.isWb()) {
					containingProcessor.getRegisterFile().setValue(rd, finalResult);
				}
				setEnableDisable();
			}
		} else {
			doOperations();
		}
	}

}
