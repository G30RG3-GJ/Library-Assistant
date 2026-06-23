package library.assistant.util;

public class FineCalculator {
    public static float calculateFine(int totalDays, int nDaysWithoutFine, float finePerDay) {
        int fineDays = totalDays - nDaysWithoutFine;
        if (fineDays > 0) {
            return fineDays * finePerDay;
        }
        return 0f;
    }
}
