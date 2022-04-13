package org.jcorp.japp.utils;

public class Utils {
    public static Boolean inRange(Number value, Number min, Number max) {
        return min.doubleValue() <= value.doubleValue()
            && value.doubleValue() < max.doubleValue();
    }
}
