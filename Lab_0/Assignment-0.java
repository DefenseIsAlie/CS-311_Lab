import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

class Main {
	// returns the state of the sensor based on p_ON (1 => ON, 0 => OFF)
	public static int getSensorState(double p_ON) {
		Random rand = new Random();
		int num = rand.nextInt(100);
		if (num <= p_ON * 100 - 1) {
			return 1;
		} else {
			return 0;
		}
	}

	// simulates the infiltrating
	public static int simulator(int width, double p_ON) {
		int[][] border = new int[width + 1][3]; // border matrix
		p_ON = Math.round(p_ON * 100.0) / 100.0; // for rounding to 2 decimal places

		// current location of the infiltrator
		int infiltrator_x = 1;
		int infiltrator_y = 0;

		// time taken by the infiltrator to reach the end
		int time_taken = 0;

		// keep on iterating until we reach the end of the border
		while (infiltrator_y != width) {
			for (int i = 1; i <= width; i++) {
				for (int j = 0; j < 3; j++) {
					border[i][j] = getSensorState(p_ON);
				}
			}
			boolean condition_1 = border[infiltrator_y + 1][infiltrator_x - 1] == 0;
			boolean condition_2 = border[infiltrator_y + 1][infiltrator_x + 1] == 0;
			boolean condition_3 = border[infiltrator_y + 1][infiltrator_x] == 0;

			if (condition_1 || condition_2 || condition_3) {
				infiltrator_y++; // move forward
			}

			// if all the conditions are false he chooses to remain at the same position
			time_taken += 10;
		}
		return time_taken;
	}

	public static void main(String[] args) {

		// creating probability.txt if not exists
		try {
			File myObj = new File("probability.txt");
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println(myObj.getName() + " already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		// creating width.txt if not exists
		try {
			File myObj = new File("width.txt");
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println(myObj.getName() + " already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		// storing the results in probability.txt
		try {
			FileWriter myWriter = new FileWriter("probability.txt");
			int width = 500;
			double p_ON = 0.01;
			while (p_ON != 0.96) {
				int time_taken = simulator(width, p_ON);
				myWriter.append(String.valueOf(p_ON) + " ");
				myWriter.append(String.valueOf(time_taken) + "\n");
				p_ON += 0.01;
				p_ON = Math.round(p_ON * 100.0) / 100.0; // for rounding to 2 decimal place
			}
			myWriter.close();
			System.out.println("Successfully wrote into the probability.txt.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		// storing the results in width.txt
		try {
			double p_ON = 0.5;
			int width = 10;
			FileWriter myWriter = new FileWriter("width.txt");
			while (width != 500) {
				int time_taken = simulator(width, p_ON);
				myWriter.append(String.valueOf(width) + " ");
				myWriter.append(String.valueOf(Math.round(time_taken)) + "\n");
				width += 10;
			}
			myWriter.close();
			System.out.println("Successfully wrote into the width.txt.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
}
