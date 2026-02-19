package library.assistant.ui.settings;
public class Preferences {
    public static Preferences getPreferences() { return new Preferences(); }
    public int getnDaysWithoutFine() { return 10; }
    public float getFinePerDay() { return 2.0f; }
}
