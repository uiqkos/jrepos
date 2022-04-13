import org.jcorp.jcalc.Calculation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculationTest {
    private final Calculation calculation = new Calculation();

    @Test
    @DisplayName("Тестирование входящего типа продукции")
    public void test1() {
        assertEquals(-1, calculation.makeCalculation(
            10, 1, 1, 1, 1
        ));
    }
    @Test
    @DisplayName("Тестирование входящего типа материала")
    public void test2() {
        assertEquals(-1, calculation.makeCalculation(
            1, 10, 1, 1, 1
        ));
    }
    @Test
    @DisplayName("Тестирование входящего типа материала")
    public void test3() {
        assertEquals(-1, calculation.makeCalculation(
            1, -0, 1, 1, 1
        ));
    }
    @Test
    @DisplayName("Тестирование входящего типа продукции")
    public void test4() {
        assertEquals(111439, calculation.makeCalculation(
            1, 1, 10, 154.44F, 65.4F
        ));
    }
    @Test
    @DisplayName("Тестирование входящего типа продукции")
    public void test5() {
        assertEquals(3819499, calculation.makeCalculation(
            2,2,805, 26.85,70.6
        ));
    }
    @Test
    @DisplayName("Тестирование входящего типа продукции")
    public void test6() {
        assertEquals(1688544, calculation.makeCalculation(
            1, 2, 326, 80.67, 58.3));
    }
}
