package processor.pipeline;

import generic.Simulator;
import processor.Processor;
import generic.Instruction;
import generic.Instruction.OperationType;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch){
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performRW(){
		if(MA_RW_Latch.isRW_enable()){
			// fetching instruction packet, ALUresult from the pipeline register
			Instruction I = MA_RW_Latch.getInstruction(); 
			int ALUresult = MA_RW_Latch.getALU_result();
			OperationType OPtype = I.getOperationType();
				
			switch(OPtype){ 
				// no need to store anything in case of these instructions
				case store:
				case jmp:
				case beq:
				case bne:
				case blt:
				case bgt:
					break;
				// in case of load instruction
				case load:
					int loadResult = MA_RW_Latch.getLoad_result();
					int rd = I.getDestinationOperand().getValue();
					containingProcessor.getRegisterFile().setValue(rd, loadResult);
					break;
				case end:
					Simulator.setSimulationComplete(true);
					break;
				default:
				// default arithmetic and logical operation case(add, addi etc..)
					rd = I.getDestinationOperand().getValue();
					containingProcessor.getRegisterFile().setValue(rd, ALUresult);
					break;
			}
			
			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);
		}
	}

}