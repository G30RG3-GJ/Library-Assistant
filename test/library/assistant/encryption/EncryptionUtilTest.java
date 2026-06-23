package library.assistant.encryption;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import static org.junit.Assert.*;

public class EncryptionUtilTest {
    private static final File KEY_FILE = new File("store/key.spec");
    private static final File BACKUP_FILE = new File("store/key.spec.bak");

    @Before
    public void setUp() throws Exception {
        if (KEY_FILE.exists()) {
            Files.move(KEY_FILE.toPath(), BACKUP_FILE.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        EncryptionUtil.init();
    }

    @After
    public void tearDown() throws Exception {
        if (KEY_FILE.exists()) {
            KEY_FILE.delete();
        }
        if (BACKUP_FILE.exists()) {
            Files.move(BACKUP_FILE.toPath(), KEY_FILE.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Test
    public void testEncrypt() {
        String original = "testString";
        String encrypted = EncryptionUtil.encrypt(original);
        assertNotNull("Encrypted string should not be null", encrypted);
        assertNotEquals("Encrypted string should not be equal to original", original, encrypted);

        String decrypted = EncryptionUtil.decrypt(encrypted);
        assertEquals("Decrypted string should match original", original, decrypted);
    }
}
