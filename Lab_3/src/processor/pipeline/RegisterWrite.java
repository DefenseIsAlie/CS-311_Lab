package processor.pipeline;

import generic.Instruction;
import generic.Simulator;
import generic.Instruction.OperationType;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType MA_RW_Latch, IF_EnableLatchType IF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = MA_RW_Latch;
		this.IF_EnableLatch = IF_EnableLatch;
	}
	
	public void performRW()
	{
		if(MA_RW_Latch.isRW_enable())
		{
			Instruction I = MA_RW_Latch.getInstruction();
			OperationType OP = I.getOperationType();
			
			// In case of branches there is no need writing into the register file
			Boolean branches = (OP == OperationType.beq || OP == OperationType.bgt || OP == OperationType.bne || OP == OperationType.blt || OP == OperationType.jmp);
		
			Boolean others = (OP == OperationType.store);

			if (OP == OperationType.load) {
				// in case of load operation loadResult is to be store in the destination register
				int loadResult = MA_RW_Latch.getLoadResult();
				int destinationRegister = I.getDestinationOperand().getValue();
				containingProcessor.getRegisterFile().setValue(destinationRegister, loadResult);
			} else if (OP == OperationType.end) {
				// if instruction being processed is an end instruction, remember to call Simulator.setSimulationComplete(true);
				Simulator.setSimulationComplete(true);
			} else if (!(branches && others)) {
				// this for usual operation like add, subtract, etc.. where ALU_result in being stored in the destination register
				int ALU_result = MA_RW_Latch.getALU_result();
				int destinationRegister = I.getDestinationOperand().getValue();
				containingProcessor.getRegisterFile().setValue(destinationRegister, ALU_result);
			}

			// Disable RW and enable IF stage to process the next instruction
			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);
		}
	}

}
