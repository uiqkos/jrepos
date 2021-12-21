package org.learn.util;

public class MathUtils {
    public static Boolean inRange(int min, int max, int value) {
        return value >= min && value < max;
    }
    public static Boolean inRange(int min, int max, double value) {
        return value >= min && value < max;
    }
}
