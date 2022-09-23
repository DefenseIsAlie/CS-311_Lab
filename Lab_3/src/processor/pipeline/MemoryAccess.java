package processor.pipeline;

import generic.Instruction;
import generic.Instruction.OperationType;
import processor.Processor;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		if (EX_MA_Latch.isMA_enable()) {
			Instruction I = EX_MA_Latch.getInstruction();
			int ALU_result = EX_MA_Latch.getALU_result();
			OperationType OP = I.getOperationType();

			if (OP == OperationType.store) {
				// address is the memory address at which a value is to be stored
				// data is the value which is to be stored (op2 which is equivalent to rd) 
				int address = ALU_result; 
				int data = containingProcessor.getRegisterFile().getValue(I.getSourceOperand2().getValue());

				// after retrieving the value and address, store it in the memory
				containingProcessor.getMainMemory().setWord(address, data);
			} else if (OP == OperationType.load) {
				/* get the value which is to be stored in either (ra, rd) from the main memory at ALU_result location and store in loadResult */
				int loadResult = containingProcessor.getMainMemory().getWord(ALU_result);
				MA_RW_Latch.setLoadResult(loadResult);
			}

			// store the necessary info regarding an instruction in MA_RW_Latch
			MA_RW_Latch.setInstruction(I);
			MA_RW_Latch.setALU_result(ALU_result);

			// set Register Write stage as enable and Memory Access stage as disable
			MA_RW_Latch.setRW_enable(true);
			EX_MA_Latch.setMA_enable(false);
		}
	}

}
