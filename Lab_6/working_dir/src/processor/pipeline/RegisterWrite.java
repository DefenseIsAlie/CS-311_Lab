package processor.pipeline;

import generic.Simulator;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performRW()
	{
		if (MA_RW_Latch.isRW_enable()) {

			if (!MA_RW_Latch.getIsNOP()) {

				int ALUresult = MA_RW_Latch.getALUresult();
				int RS1 = MA_RW_Latch.getRS1();
				int RS2 = MA_RW_Latch.getRS2();
				int RD = MA_RW_Latch.getRD();
				int IMM = MA_RW_Latch.getIMM();
				String OPCODE = MA_RW_Latch.getOPcode();

				if (MA_RW_Latch.getIsLoad()) { // if the instruction is a load, write aluresult into the register
					
					containingProcessor.getRegisterFile().setValue(RD, ALUresult);
					MA_RW_Latch.setIsLoad(false);

				} else {

					if(OPCODE.equals("11101") == false) {
						if(OPCODE.equals("11000") == false) {
							if(OPCODE.equals("11001") == false) {
								if(OPCODE.equals("11010") == false) {
									if(OPCODE.equals("11011") == false) {
										if(OPCODE.equals("11100") == false) {
											containingProcessor.getRegisterFile().setValue(RD, ALUresult);
										}
									}
								}
							}
						}
					}

				}

				MA_RW_Latch.setRW_enable(false);
				if(MA_RW_Latch.getOPcode().equals("11101")) {
					// if instruction being processed is an end instruction, remember to call Simulator.setSimulationComplete(true);
					Simulator.setSimulationComplete(true);
					IF_EnableLatch.setIF_enable(false);
				}
			}
		}
	}
}
