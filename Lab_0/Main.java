import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

import util.Helper;
import util.Infiltrator;
import util.Timer;
import util.Border;

class Main {
    public static int simulate(int width, double p_ON) {
        Helper helper = new Helper();
        p_ON = helper.roundTo2(p_ON);

        Infiltrator I = new Infiltrator(1, 0);// Infiltrator object
        Border B = new Border(width + 1); // Border Object
        Timer T = new Timer(0); // Timer object for keeping track of the time

        // keep on iterating until we reach the end of the border
        while (I.y != width) {
            for (int i = 1; i <= width; i++) {
                for (int j = 0; j < 3; j++) {
                    B.border[i][j] = helper.getSensorState(p_ON);
                }
            }
            boolean condition_1 = B.border[I.y + 1][I.x - 1] == 0;
            boolean condition_2 = B.border[I.y + 1][I.x + 1] == 0;
            boolean condition_3 = B.border[I.y + 1][I.x] == 0;
            if (condition_1 || condition_2 || condition_3) {
                I.y++; // move forward
            }
            // if all the conditions are false he chooses to remain at the same position
            T.time += 10;
        }
        return T.time;
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
                int time_taken = simulate(width, p_ON);
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
                int time_taken = simulate(width, p_ON);
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