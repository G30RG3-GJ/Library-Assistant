package library.assistant.encryption;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EncryptionUtilTest {

    private static final String KEY_STORE_PATH = "store/key.spec";
    private File keyStore;
    private File backupKeyStore;

    @Before
    public void setUp() throws IOException {
        keyStore = new File(KEY_STORE_PATH);
        backupKeyStore = new File(KEY_STORE_PATH + ".bak");

        // Backup existing key file
        if (keyStore.exists()) {
            Files.copy(keyStore.toPath(), backupKeyStore.toPath(), StandardCopyOption.REPLACE_EXISTING);
            keyStore.delete();
        }
    }

    @After
    public void tearDown() throws IOException {
        // Clean up current key store
        if (keyStore.exists()) {
             keyStore.delete();
        }

        // Restore backup
        if (backupKeyStore.exists()) {
            Files.copy(backupKeyStore.toPath(), keyStore.toPath(), StandardCopyOption.REPLACE_EXISTING);
            backupKeyStore.delete();
        }
    }

    @Test
    public void testInitAndEncryption() throws Exception {
        // Test Initialization
        EncryptionUtil.init();
        Assert.assertTrue("Key store should be created", keyStore.exists());

        // Test Encryption and Decryption
        String originalText = "Hello World";
        String encryptedText = EncryptionUtil.encrypt(originalText);

        Assert.assertNotNull(encryptedText);
        Assert.assertNotEquals(originalText, encryptedText);

        String decryptedText = EncryptionUtil.decrypt(encryptedText);
        Assert.assertEquals(originalText, decryptedText);
    }
}
