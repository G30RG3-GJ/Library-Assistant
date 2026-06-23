package library.assistant.ui.settings;

import org.junit.Test;
import static org.junit.Assert.*;

public class PreferencesTest {

    @Test
    public void testSetPasswordShort() {
        Preferences pref = new Preferences();
        String shortPassword = "password123"; // 11 chars
        pref.setPassword(shortPassword);

        // SHA-1 of "password123"
        String expectedHash = "cbfdac6008f9cab4083784cbd1874f76618d2a97";
        assertEquals(expectedHash, pref.getPassword());
    }

    @Test
    public void testSetPasswordLong() {
        Preferences pref = new Preferences();
        String longPassword = "thisisalongpassword16"; // 21 chars
        pref.setPassword(longPassword);

        assertEquals(longPassword, pref.getPassword());
    }

    @Test
    public void testSetPasswordBoundary15() {
        Preferences pref = new Preferences();
        String password = "123456789012345"; // 15 chars
        pref.setPassword(password);

        // SHA-1 of "123456789012345"
        String expectedHash = "65cc4c0b6cf9c56e2a2d801df1b99dc933db9991";
        assertEquals(expectedHash, pref.getPassword());
    }

    @Test
    public void testSetPasswordBoundary16() {
        Preferences pref = new Preferences();
        String password = "1234567890123456"; // 16 chars
        pref.setPassword(password);

        assertEquals(password, pref.getPassword());
    }
}
