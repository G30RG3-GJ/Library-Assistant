package library.assistant.encryption;

import org.junit.Test;
import static org.junit.Assert.*;

public class CipherSpecTest {

    @Test
    public void testIsValid() {
        byte[] key = new byte[16];
        byte[] iv = new byte[16];

        // Test valid case
        CipherSpec spec = new CipherSpec(key, iv);
        assertTrue("Should be valid when key and iv are present", spec.isValid());

        // Test null key
        spec = new CipherSpec(null, iv);
        assertFalse("Should be invalid when key is null", spec.isValid());

        // Test null iv
        spec = new CipherSpec(key, null);
        assertFalse("Should be invalid when iv is null", spec.isValid());

        // Test both null
        spec = new CipherSpec(null, null);
        assertFalse("Should be invalid when both are null", spec.isValid());
    }
}
