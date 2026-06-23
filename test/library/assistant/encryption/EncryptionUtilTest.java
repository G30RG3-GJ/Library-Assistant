package library.assistant.encryption;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EncryptionUtilTest {

    private static final String STORE_PATH = "store/key.spec";
    private File backupFile;
    private File originalFile;

    @Before
    public void setUp() throws Exception {
        originalFile = new File(STORE_PATH);
        if (originalFile.exists()) {
            backupFile = new File(STORE_PATH + ".bak");
            Files.copy(originalFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        // Ensure fresh start
        EncryptionUtil.init();
    }

    @After
    public void tearDown() throws Exception {
        if (backupFile != null && backupFile.exists()) {
            Files.copy(backupFile.toPath(), originalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            backupFile.delete();
        }
    }

    @Test
    public void testEncryptDecrypt() {
        String originalText = "Hello World";
        String encryptedText = EncryptionUtil.encrypt(originalText);
        Assert.assertNotNull(encryptedText);
        Assert.assertNotEquals(originalText, encryptedText);

        String decryptedText = EncryptionUtil.decrypt(encryptedText);
        Assert.assertEquals(originalText, decryptedText);
    }

    @Test
    public void testDecryptionFailure() {
        String invalidEncrypted = "invalid_base64_string";
        String decrypted = EncryptionUtil.decrypt(invalidEncrypted);
        Assert.assertNull(decrypted);
    }
}
