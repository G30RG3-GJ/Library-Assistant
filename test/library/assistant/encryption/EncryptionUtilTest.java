package library.assistant.encryption;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EncryptionUtilTest {

    private static final File KEY_STORE = new File("store/key.spec");
    private static final File KEY_STORE_BACKUP = new File("store/key.spec.bak");

    @Before
    public void setUp() throws IOException {
        if (KEY_STORE.exists()) {
            Files.copy(KEY_STORE.toPath(), KEY_STORE_BACKUP.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @After
    public void tearDown() throws IOException {
        if (KEY_STORE_BACKUP.exists()) {
            Files.move(KEY_STORE_BACKUP.toPath(), KEY_STORE.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } else if (KEY_STORE.exists()) {
            KEY_STORE.delete();
        }
    }

    @Test
    public void testEncryptAndDecrypt() throws Exception {
        EncryptionUtil.init();
        String plainText = "Hello World";
        String encrypted = EncryptionUtil.encrypt(plainText);

        assertNotNull(encrypted);
        assertNotEquals(plainText, encrypted);

        String decrypted = EncryptionUtil.decrypt(encrypted);
        assertEquals(plainText, decrypted);
    }

    @Test
    public void testEncryptNull() {
        String result = EncryptionUtil.encrypt(null);
        assertNull(result);
    }
}
