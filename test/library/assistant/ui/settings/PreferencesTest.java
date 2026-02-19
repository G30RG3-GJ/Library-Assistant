package library.assistant.ui.settings;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.digest.DigestUtils;

public class PreferencesTest {

    @Test
    public void testSetPasswordShort() {
        Preferences pref = new Preferences();
        String password = "short";
        pref.setPassword(password);
        assertEquals("Short password should be hashed", DigestUtils.shaHex(password), pref.getPassword());
    }

    @Test
    public void testSetPasswordLong() {
        Preferences pref = new Preferences();
        String password = "verylongpasswordmorethan16chars";
        pref.setPassword(password);
        assertEquals("Long password should be hashed", DigestUtils.shaHex(password), pref.getPassword());
    }
}
