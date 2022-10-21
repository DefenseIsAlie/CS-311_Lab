package generic;

import java.io.PrintWriter;

public class Statistics {
	
	// TODO add your statistics here
	public static int noOfInstructions;
	static int noOfCycles;
	public static int dataHazard = 0;
	public static int controlHazard = 0;

	public static void printStatistics(String statFile) {
		try {
			PrintWriter writer = new PrintWriter(statFile);
			writer.println("# cycles : " + noOfCycles);
			writer.println("# data hazards : " + dataHazard );
			writer.println("# control hazards : " + controlHazard ) ;
			writer.println("# Instructions : " + noOfInstructions);			
			writer.close();
		}
		catch(Exception e) {
			Misc.printErrorAndExit(e.getMessage());
		}
	}
	
	// TODO write functions to update statistics
	public static void setNumberOfInstructions(int noOfInstructions) {
		Statistics.noOfInstructions = noOfInstructions;
	}
	public static  void setNumberOfCycles(int noOfCycles) {
		Statistics.noOfCycles = noOfCycles;
	}
}
