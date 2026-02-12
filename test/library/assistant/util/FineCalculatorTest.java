package library.assistant.util;

public class FineCalculatorTest {
    public static void main(String[] args) {
        testNoFine();
        testNoFineBoundary();
        testFine();
        testNoFinePerDay();
        System.out.println("All tests passed!");
    }

    private static void testNoFine() {
        float fine = FineCalculator.calculateFine(5, 10, 2.0f);
        if (fine != 0.0f) {
            throw new RuntimeException("Test Failed: Expected 0.0, got " + fine);
        }
    }

    private static void testNoFineBoundary() {
        float fine = FineCalculator.calculateFine(10, 10, 2.0f);
        if (fine != 0.0f) {
            throw new RuntimeException("Test Failed: Expected 0.0, got " + fine);
        }
    }

    private static void testFine() {
        float fine = FineCalculator.calculateFine(12, 10, 2.0f);
        if (Math.abs(fine - 4.0f) > 0.001f) {
            throw new RuntimeException("Test Failed: Expected 4.0, got " + fine);
        }
    }

    private static void testNoFinePerDay() {
        float fine = FineCalculator.calculateFine(12, 10, 0.0f);
        if (fine != 0.0f) {
            throw new RuntimeException("Test Failed: Expected 0.0, got " + fine);
        }
    }
}
