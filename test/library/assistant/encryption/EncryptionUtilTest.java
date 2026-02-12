package library.assistant.encryption;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EncryptionUtilTest {

    @Before
    public void setUp() throws Exception {
        // Ensure encryption is initialized before each test
        EncryptionUtil.init();
    }

    @Test
    public void testEncryptDecrypt() {
        String originalText = "This is a secret message used for testing encryption 123!@#";

        // Test Encryption
        String encryptedText = EncryptionUtil.encrypt(originalText);
        Assert.assertNotNull("Encryption returned null", encryptedText);
        Assert.assertNotEquals("Encrypted text matches original text", originalText, encryptedText);

        // Test Decryption
        String decryptedText = EncryptionUtil.decrypt(encryptedText);
        Assert.assertNotNull("Decryption returned null", decryptedText);

        // Verification
        Assert.assertEquals("Decrypted text does not match original", originalText, decryptedText);
    }
}
