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
            for (int i = 1; i <= width; i++) {
                for (int j = 0; j < 3; j++) {
                    B.border[i][j] = H.getSensorState(p_ON);
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
}
