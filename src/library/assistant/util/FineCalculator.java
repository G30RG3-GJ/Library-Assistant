package library.assistant.util;

public class FineCalculator {
    public static float calculateFine(int totalDays, int nDaysWithoutFine, float finePerDay) {
        float fine = 0f;
        int fineDays = totalDays - nDaysWithoutFine;
        if (fineDays > 0) {
            fine = fineDays * finePerDay;
        }
        return fine;
    }
}
