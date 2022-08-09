package simulator;

import util.Helper;
import util.Infiltrator;
import util.Timer;
import util.Border;

// simulates the infiltrating
public class Simulator {

    public int simulate(int width, double p_ON) {
        Helper H = new Helper(); // Helper object which has contains some helper methods
        p_ON = H.roundTo2(p_ON);

        Infiltrator I = new Infiltrator(1, 0);// Infiltrator object
        Border B = new Border(width + 1); // Border Object
        Timer T = new Timer(0); // Timer object for keeping track of the time

        // keep on iterating until we reach the end of the border
        while (I.y != width) {
            if (I.y == 0) { // Infiltration has to not yet entered the border
                for (int j = 0; j < 3; j++) {
                    B.border[I.y + 1][j] = H.getSensorState(p_ON);
                }
            } else { // Infiltrator has already entered the border
                for (int j = 0; j < 3; j++) {
                    B.border[I.y + 1][j] = H.getSensorState(p_ON);
                }
                B.border[I.y][I.x] = H.getSensorState(p_ON); // current position
            }
            boolean condition_1 = B.border[I.y + 1][I.x - 1] == 0;
            boolean condition_2 = B.border[I.y + 1][I.x + 1] == 0;
            boolean condition_3 = B.border[I.y + 1][I.x] == 0;
            if (I.y == 0) { // Infiltration has to not yet entered the border
                if (condition_1 || condition_2 || condition_3) {
                    I.y++; // move forward
                }
            } else {
                // Infiltrator has already entered the border
                boolean condition_4 = B.border[I.y][I.x] == 0; // Current position of the infiltrator
                if (condition_4) { // He can move to other safe cells only if the current cell has its sensor in
                                   // OFF state
                    if (condition_1 || condition_2 || condition_3) {
                        I.y++; // move forward
                    }
                }
            }
            // if all the conditions are false he chooses to remain at the same position
            T.time += 10;
        }
        return T.time;
    }
}
