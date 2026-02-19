package library.assistant.encryption;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EncryptionUtilTest {

    private static final String KEY_FILE_PATH = "store/key.spec";
    private File keyFile;
    private File backupFile;

    @Before
    public void setUp() throws Exception {
        keyFile = new File(KEY_FILE_PATH);
        backupFile = new File(KEY_FILE_PATH + ".bak");

        if (keyFile.exists()) {
            Files.copy(keyFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } else {
            // Ensure store directory exists
            if (keyFile.getParentFile() != null) {
                keyFile.getParentFile().mkdirs();
            }
        }

        // Ensure encryption is initialized (key file exists)
        EncryptionUtil.init();
    }

    @After
    public void tearDown() throws Exception {
        if (backupFile.exists()) {
            Files.copy(backupFile.toPath(), keyFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            backupFile.delete();
        } else {
            // If backup didn't exist, we assume no key existed before test.
            // So we should delete the one created by test.
            if (keyFile.exists()) {
                 keyFile.delete();
            }
        }
    }

    @Test
    public void testEncryptionDecryption() {
        String originalText = "Hello World! This is a test.";
        String encryptedText = EncryptionUtil.encrypt(originalText);

        assertNotNull("Encrypted text should not be null", encryptedText);
        assertNotEquals("Encrypted text should be different from original", originalText, encryptedText);

        String decryptedText = EncryptionUtil.decrypt(encryptedText);
        assertEquals("Decrypted text should match original", originalText, decryptedText);
    }

    @Test
    public void testEmptyString() {
        String originalText = "";
        String encryptedText = EncryptionUtil.encrypt(originalText);

        assertNotNull(encryptedText);
        String decryptedText = EncryptionUtil.decrypt(encryptedText);
        assertEquals(originalText, decryptedText);
    }

    @Test
    public void testSpecialCharacters() {
        String originalText = "!@#$%^&*()_+{}:\"<>?|~`";
        String encryptedText = EncryptionUtil.encrypt(originalText);

        assertNotNull(encryptedText);
        String decryptedText = EncryptionUtil.decrypt(encryptedText);
        assertEquals(originalText, decryptedText);
    }
}
