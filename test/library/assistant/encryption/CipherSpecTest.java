package library.assistant.encryption;

import org.junit.Test;
import static org.junit.Assert.*;

public class CipherSpecTest {

    @Test
    public void testIsValidWithValidKeyAndIV() {
        byte[] key = "secretkey".getBytes();
        byte[] iv = "initialvector".getBytes();
        CipherSpec spec = new CipherSpec(key, iv);
        assertTrue("CipherSpec should be valid when key and IV are not null", spec.isValid());
    }

    @Test
    public void testIsValidWithNullKey() {
        byte[] iv = "initialvector".getBytes();
        CipherSpec spec = new CipherSpec(null, iv);
        assertFalse("CipherSpec should be invalid when key is null", spec.isValid());
    }

    @Test
    public void testIsValidWithNullIV() {
        byte[] key = "secretkey".getBytes();
        CipherSpec spec = new CipherSpec(key, null);
        assertFalse("CipherSpec should be invalid when IV is null", spec.isValid());
    }

    @Test
    public void testIsValidWithNullKeyAndIV() {
        CipherSpec spec = new CipherSpec(null, null);
        assertFalse("CipherSpec should be invalid when both key and IV are null", spec.isValid());
    }
}
