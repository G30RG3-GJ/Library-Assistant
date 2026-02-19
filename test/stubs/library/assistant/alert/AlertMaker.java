package library.assistant.alert;

public class AlertMaker {

    public static void showSimpleAlert(String title, String content) {
        System.out.println("STUB: AlertMaker.showSimpleAlert: " + title + " - " + content);
    }

    public static void showErrorMessage(Exception ex, String title, String content) {
        System.out.println("STUB: AlertMaker.showErrorMessage: " + title + " - " + content);
        if (ex != null) {
            ex.printStackTrace();
        }
    }
}
