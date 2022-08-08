package util;

import java.util.*;

public class Helper {
    // returns the state of the sensor based on p_ON (1 => ON, 0 => OFF)
    public int getSensorState(double p_ON) {
        Random rand = new Random();
        int num = rand.nextInt(100);
        if (num <= p_ON * 100 - 1) {
            return 1;
        } else {
            return 0;
        }
    }

    // for rounding to 2 decimal places
    public double roundTo2(double x) {
        return Math.round(x * 100.0) / 100.0;
    }
}
