package library.assistant.ui.settings;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.digest.DigestUtils;

public class PreferencesTest {

    @Test
    public void testSetPassword_ShortPassword_Hashed() {
        Preferences preferences = new Preferences();
        String password = "shortPassword";
        preferences.setPassword(password);

        String expectedHash = DigestUtils.shaHex(password);
        assertEquals("Password shorter than 16 chars should be hashed", expectedHash, preferences.getPassword());
    }

    @Test
    public void testSetPassword_LongPassword_NotHashed() {
        Preferences preferences = new Preferences();
        String password = "thisIsAVeryLongPasswordThatIsMoreThan16Chars";
        preferences.setPassword(password);

        assertEquals("Password longer than or equal to 16 chars should be stored as is", password, preferences.getPassword());
    }

    @Test
    public void testSetPassword_Exact16Chars_NotHashed() {
        Preferences preferences = new Preferences();
        String password = "1234567890123456"; // 16 chars
        preferences.setPassword(password);

        assertEquals("Password of exactly 16 chars should be stored as is", password, preferences.getPassword());
    }

    @Test
    public void testSetPassword_EmptyPassword_Hashed() {
        Preferences preferences = new Preferences();
        String password = "";
        preferences.setPassword(password);

        String expectedHash = DigestUtils.shaHex(password);
        assertEquals("Empty password should be hashed", expectedHash, preferences.getPassword());
    }
}
