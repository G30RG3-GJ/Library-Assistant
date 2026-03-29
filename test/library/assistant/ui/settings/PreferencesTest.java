package library.assistant.ui.settings;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.digest.DigestUtils;

public class PreferencesTest {

    @Test
    public void testDefaultConstructor() {
        Preferences prefs = new Preferences();
        // The default implementation should not set credentials.
        assertNull("Username should be null by default", prefs.getUsername());
        assertNull("Password should be null by default", prefs.getPassword());
    }

    @Test
    public void testSetPasswordShort() {
        Preferences prefs = new Preferences();
        String shortPass = "short";
        prefs.setPassword(shortPass);

        // "short" length is 5 < 16, so it should be hashed
        assertNotEquals(shortPass, prefs.getPassword());
        assertEquals(40, prefs.getPassword().length()); // SHA-1 hex is 40 chars
        assertEquals(DigestUtils.shaHex(shortPass), prefs.getPassword());
    }

    @Test
    public void testSetPasswordLong() {
        Preferences prefs = new Preferences();
        String longPass = "1234567890123456"; // 16 chars
        prefs.setPassword(longPass);

        // >= 16 chars -> plain text
        assertEquals(longPass, prefs.getPassword());
    }
}
