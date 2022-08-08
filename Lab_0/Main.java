
// Inbuilt libraries
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

// Custom libraries
import simulator.Simulator;
import util.Helper;

class Main {
    public static void main(String[] args) {
        // Creating probability.txt if not exists
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

        // Creating width.txt if not exists
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

        Simulator S = new Simulator(); // This object has a simulate method which will be used to simulate the
                                       // infiltration
        Helper H = new Helper(); // This object has some helper methods in it

        // Storing the results in probability.txt
        try {
            int width = 500; // Width of the border
            double p_ON = 0.01; // Probability that the sensor will be ON for next 10 seconds
            FileWriter myWriter = new FileWriter("probability.txt");
            while (p_ON != 0.96) {
                int time_taken = S.simulate(width, p_ON);
                myWriter.append(String.valueOf(p_ON) + " ");
                myWriter.append(String.valueOf(time_taken) + "\n");
                p_ON += 0.01;
                p_ON = H.roundTo2(p_ON);
            }
            myWriter.close();
            System.out.println("Successfully wrote into the probability.txt");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // Storing the results in width.txt
        try {
            double p_ON = 0.5;
            int width = 10;
            FileWriter myWriter = new FileWriter("width.txt");
            while (width != 500) {
                int time_taken = S.simulate(width, p_ON);
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