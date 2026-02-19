package library.assistant.encryption;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class EncryptionUtilTest {

    private static final String KEY_STORE_PATH = "store/key.spec";
    private File keyStoreFile;
    private File backupFile;

    @Before
    public void setUp() throws Exception {
        keyStoreFile = new File(KEY_STORE_PATH);
        if (keyStoreFile.exists()) {
            backupFile = new File(KEY_STORE_PATH + ".bak");
            Files.copy(keyStoreFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        // Ensure clean slate for test if needed, or rely on existing setup.
        // EncryptionUtil.init() will use existing file or create new one.
        // We'll call init() to ensure it's ready.
        try {
            EncryptionUtil.init();
        } catch (Exception e) {
            fail("Failed to initialize EncryptionUtil: " + e.getMessage());
        }
    }

    @After
    public void tearDown() throws Exception {
        if (backupFile != null && backupFile.exists()) {
            Files.copy(backupFile.toPath(), keyStoreFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            backupFile.delete();
        } else {
             // If we created it and no backup existed, maybe we should delete it?
             // But for now, restoring backup is safer if it existed.
             // If it didn't exist, we might want to clean up.
             if (backupFile == null && keyStoreFile.exists()) {
                 keyStoreFile.delete();
             }
        }
    }

    @Test
    public void testConcurrency() throws InterruptedException {
        int numThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                try {
                    String encrypted = EncryptionUtil.encrypt("test_string");
                    if (encrypted != null) {
                        successCount.incrementAndGet();
                    } else {
                        failureCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Print stack trace to see the error
                    failureCount.incrementAndGet();
                }
            });
        }

        executor.shutdown();
        if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
            fail("Executor did not terminate in time.");
        }

        assertEquals("All threads should succeed", numThreads, successCount.get());
        assertEquals("No threads should fail", 0, failureCount.get());
    }
}
