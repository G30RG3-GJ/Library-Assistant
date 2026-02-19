package library.assistant.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class FineCalculatorTest {

    @Test
    public void testCalculateFine() {
        // No fine: total days <= days without fine
        assertEquals(0f, FineCalculator.calculateFine(10, 14, 2f), 0.001);
        assertEquals(0f, FineCalculator.calculateFine(14, 14, 2f), 0.001);

        // Fine: total days > days without fine
        // 15 days total, 14 days grace -> 1 day fine * 2 = 2
        assertEquals(2f, FineCalculator.calculateFine(15, 14, 2f), 0.001);

        // 20 days total, 14 days grace -> 6 days fine * 2 = 12
        assertEquals(12f, FineCalculator.calculateFine(20, 14, 2f), 0.001);

        // Different fine amount
        assertEquals(30f, FineCalculator.calculateFine(20, 10, 3f), 0.001);
    }
}
