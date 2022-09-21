package generic;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import processor.Clock;
import processor.Processor;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		try {
			loadProgram(assemblyProgramFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		simulationComplete = false;
	}
	
	static void loadProgram(String assemblyProgramFile) throws IOException
	{
		InputStream input = null;
		try {
			input = new FileInputStream(assemblyProgramFile);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}

		DataInputStream binaryDataInput = new DataInputStream(input);
		int currentAdress = 0;

		Boolean isPC = true;

		/*
		1. Load the program into memory according to the program layout described in the ISA specification
		*/
		while (binaryDataInput.available() > 0) {
			int currentMemoryContent = binaryDataInput.readInt();
			if (isPC) {
				/*
				2. set PC to the address of the first instruction in the main memory 
				*/
				processor.getRegisterFile().setProgramCounter(currentMemoryContent);
				isPC = false;
				continue;
			}
			processor.getMainMemory().setWord(currentAdress, currentMemoryContent);
			currentAdress++;
		}

		/* 
		3. set the following registers:
		    x0 = 0
		    x1 = 65535
		    x2 = 65535
		*/
		processor.getRegisterFile().setValue(0, 0);
		processor.getRegisterFile().setValue(1, 65535);
		processor.getRegisterFile().setValue(2, 65535);
	}
	
	public static void simulate()
	{
		while(simulationComplete == false)
		{
			processor.getIFUnit().performIF();
			Clock.incrementClock();
			processor.getOFUnit().performOF();
			Clock.incrementClock();
			processor.getEXUnit().performEX();
			Clock.incrementClock();
			processor.getMAUnit().performMA();
			Clock.incrementClock();
			processor.getRWUnit().performRW();
			Clock.incrementClock();

			/*
				After processing one instruction completely through all the five units, one cycle is completed.
			*/

			/* Set statistics */
			Statistics.setNumberOfCycles(Statistics.getNumberOfCycles() + 1);
			Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions() + 1);
		}	
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
