package processor.pipeline;

import generic.Simulator;
import generic.Statistics;
import processor.Processor;
import generic.Instruction;
import generic.Instruction.OperationType;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;

	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch,
			IF_EnableLatchType iF_EnableLatch) {
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}

	public void performRW() {
		if (MA_RW_Latch.getIsNOP()) // if the instruction is a NOP
		{ 
			// no need to do anything in this stage and set isNOP to false (default) for further instructions
			MA_RW_Latch.setIsNOP(false); 
		} 
		else if (MA_RW_Latch.isRW_enable()) 
		{
			// setting up the statistics for number of RW instructions
			Statistics.setnumberOfRegisterWriteInstructions(Statistics.getNumberOfRegisterWriteInstructions() + 1);

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

			if (OPtype.ordinal() != 29) {
				// if the instruction is not end, enable the IF stage for further processing the instructions
				IF_EnableLatch.setIF_enable(true);
			}
		}
	}

}
