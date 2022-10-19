package generic;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import processor.Clock;
import processor.Processor;
import generic.Statistics;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p){
		Simulator.processor = p;
		try{
			loadProgram(assemblyProgramFile);
		} 
		catch (IOException e){
			e.printStackTrace();
		}
		simulationComplete = false;
	}
	
	static void loadProgram(String assemblyProgramFile) throws IOException{
	

		InputStream IP = null;
		try{
			IP = new FileInputStream(assemblyProgramFile);
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
		
		DataInputStream dataInput = new DataInputStream(IP);

		/* 1. load the program into memory according to the program layout described in the ISA specification */
		int address = 0;
		boolean isPC = true;
		while(dataInput.available() > 0){
			int data = dataInput.readInt();
			if (isPC) {
				/* 2. set PC to the address of the first instruction in the main */
				processor.getRegisterFile().setProgramCounter(data);
				isPC = false;
				continue;
			}
			processor.getMainMemory().setWord(address, data);
			address += 1;
		}
        
		/* 3. set the following registers:
		 *     x0 = 0
		 *     x1 = 65535
		 *     x2 = 65535
		 */
        processor.getRegisterFile().setValue(0, 0);
        processor.getRegisterFile().setValue(1, 65535);
        processor.getRegisterFile().setValue(2, 65535);
	}
			
	public static void simulate() {
		while(simulationComplete == false) {
			processor.getRWUnit().performRW();
			Clock.incrementClock();
			processor.getMAUnit().performMA();
			Clock.incrementClock();	
			processor.getEXUnit().performEX();
			Clock.incrementClock();	
			processor.getOFUnit().performOF();
			Clock.incrementClock();
			processor.getIFUnit().performIF();
			Clock.incrementClock();

			// setting up the statistics
			Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions() + 1);
			Statistics.setNumberOfCycles(Statistics.getNumberOfCycles() + 1);
		}
		
		System.out.println("");
		System.out.println("# cycles taken : " + Statistics.getNumberOfCycles());
		System.out.println("# stalls : " + (Statistics.getNumberOfInstructions() - Statistics.getNumberOfRegisterWriteInstructions()));
		System.out.println("# wrong branch instructions: " + Statistics.getNumberOfBranchTaken());
	}
	
	public static void setSimulationComplete(boolean value) {
		simulationComplete = value;
	}
}
