package org.jcorp.jcalc;

import static java.lang.Math.*;

public class Calculation {
    public int makeCalculation(int productType, int materialType, int count, double width, double length) {
        double d =
            (new double[]{0.0, 1.1, 2.5, 8.43, 0.0})[min(4, max(0, productType))] /
            (new double[]{-1, 99.7, 99.88, -1})[min(3, max(0, materialType))] * 100;
        return (d <= 0 || count < 0 || width <= 0 || length <= 0) ? -1 : (int) ceil(d * count * width * length);
    }

    public int makeCalculations(int productType, int materialType, int count, int width, int height) {
        var d = (new double[]{0.0, 1.1, 2.5, 8.43, 0.0})[min(4, max(0, productType))] /
            (new double[]{-1, 99.7, 99.88, -1})[min(0, )]
    }
}
