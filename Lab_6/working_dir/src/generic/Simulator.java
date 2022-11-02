package generic;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


import processor.Clock;
import processor.Processor;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	static EventQueue eventQueue;
	public static long storeresp;
	public static int ins_count;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p) throws FileNotFoundException
	{
		eventQueue = new EventQueue();
		storeresp = 0;
		ins_count = 0;
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		simulationComplete = false;
	}
	
	static void loadProgram(String assemblyProgramFile) throws FileNotFoundException {

			/*
			* TODO
			* 1. load the program into memory according to the program layout described
			*    in the ISA specification
			* 2. set PC to the address of the first instruction in the main
			* 3. set the following registers:
			*     x0 = 0
			*     x1 = 65535
			*     x2 = 65535
			*/
		 
		DataInputStream BIN_IP = new DataInputStream(new BufferedInputStream(new FileInputStream(assemblyProgramFile)));
		
		try{
			int N = BIN_IP.readInt();
			int idx = 0;

			for(; idx < N; idx++){
				int data = BIN_IP.readInt();
				processor.getMainMemory().setWord(idx,data);
			}
			
			int PC = idx;
			int offSet = 1;
			processor.getRegisterFile().setProgramCounter(PC);

			while(BIN_IP.available()>0){
				int data = BIN_IP.readInt();
				processor.getMainMemory().setWord(idx,data);
				idx += offSet;
			}
			
			processor.getRegisterFile().setValue(1,65535);
			processor.getRegisterFile().setValue(2,65535);
			processor.getRegisterFile().setValue(0,0);		
			BIN_IP.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static EventQueue getEventQueue() { 
		return eventQueue ; 
	}
	
	public static void simulate() {
		int cycles = 0;
		
		while(Simulator.simulationComplete == false) {
			processor.getRWUnit().performRW();
			processor.getMAUnit().performMA();
			processor.getEXUnit().performEX();
			eventQueue.processEvents();
			processor.getOFUnit().performOF();
			processor.getIFUnit().performIF();
			Clock.incrementClock();
			cycles += 1;
		}
		
		// TODO
		// set statistics
		Statistics.setNumberOfCycles(cycles);
		Statistics.setNumberOfInstructions(ins_count);
		Statistics.setCPI();
		
	}
	
	public static void setSimulationComplete(boolean value)	{
		simulationComplete = value;
	}

}