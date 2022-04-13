package org.siz.util;

public class RangeUtils {
    public static boolean inRange(int value, int left, int right) {
        return value >= left && value < right;
    }
    public static boolean inRange(double value, int left, int right) {
        return value >= left && value < right;
    }
}
