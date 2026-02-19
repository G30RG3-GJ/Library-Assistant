package library.assistant.ui.settings;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.digest.DigestUtils;

public class PreferencesTest {

    @Test
    public void testDefaultConstructorNoCredentials() {
        Preferences prefs = new Preferences();
        assertNull("Username should be null by default", prefs.getUsername());
        assertNull("Password should be null by default", prefs.getPassword());
    }

    @Test
    public void testSetPasswordHashing() {
        Preferences prefs = new Preferences();
        String password = "short";
        prefs.setPassword(password);

        String expectedHash = DigestUtils.shaHex(password);
        assertEquals("Short password should be hashed", expectedHash, prefs.getPassword());
    }

    @Test
    public void testSetPasswordNoHashing() {
        Preferences prefs = new Preferences();
        // A string that is already a hash (>= 16 chars)
        String longPassword = "thisisalongpasswordthatshouldnotbehashed";
        prefs.setPassword(longPassword);

        assertEquals("Long password should not be hashed again", longPassword, prefs.getPassword());
    }
}
