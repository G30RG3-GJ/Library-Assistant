package library.assistant.alert;

/**
 * Stub for AlertMaker to allow testing without JavaFX dependencies.
 * This class mocks the methods used by the application logic during tests.
 */
public class AlertMaker {
    public static void showErrorMessage(String title, String content) {
        System.err.println("MOCK ALERT: " + title + " - " + content);
    }
}
