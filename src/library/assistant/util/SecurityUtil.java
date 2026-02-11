package library.assistant.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SecurityUtil {

    private static final Logger LOGGER = LogManager.getLogger(SecurityUtil.class.getName());
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int AES_KEY_SIZE = 128;
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;
    // Default hardcoded key source for simplicity in this context.
    // In a production environment, use a secure key management system.
    private static final String DEFAULT_KEY_SOURCE = "LibraryAssistantSecretKeySource";

    private static SecretKey secretKey;

    static {
        try {
            String keySource = System.getenv("APP_SECRET_KEY");
            if (keySource == null || keySource.isEmpty()) {
                keySource = DEFAULT_KEY_SOURCE;
            }
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = digest.digest(keySource.getBytes(StandardCharsets.UTF_8));
            // Use only first 128 bits (16 bytes) for AES-128
            byte[] aesKey = new byte[AES_KEY_SIZE / 8];
            System.arraycopy(keyBytes, 0, aesKey, 0, aesKey.length);
            secretKey = new SecretKeySpec(aesKey, "AES");
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.log(Level.ERROR, "Error initializing SecurityUtil key", ex);
        }
    }

    public static String encrypt(String plaintext) {
        if (plaintext == null || plaintext.isEmpty()) {
            return plaintext;
        }
        try {
            byte[] iv = new byte[GCM_IV_LENGTH];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            byte[] encrypted = new byte[iv.length + ciphertext.length];
            System.arraycopy(iv, 0, encrypted, 0, iv.length);
            System.arraycopy(ciphertext, 0, encrypted, iv.length, ciphertext.length);

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            LOGGER.log(Level.ERROR, "Error encrypting data", ex);
            return null;
        }
    }

    public static String decrypt(String encryptedText) {
        if (encryptedText == null || encryptedText.isEmpty()) {
            return encryptedText;
        }
        try {
            byte[] decoded = Base64.getDecoder().decode(encryptedText);

            if (decoded.length < GCM_IV_LENGTH) {
                // Too short to contain IV, likely legacy plaintext
                return encryptedText;
            }

            byte[] iv = new byte[GCM_IV_LENGTH];
            System.arraycopy(decoded, 0, iv, 0, iv.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            byte[] ciphertext = new byte[decoded.length - iv.length];
            System.arraycopy(decoded, iv.length, ciphertext, 0, ciphertext.length);

            byte[] plaintext = cipher.doFinal(ciphertext);

            return new String(plaintext, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException | javax.crypto.AEADBadTagException ex) {
            // Likely legacy plaintext or corrupted data
            // Return original text as fallback for legacy support
            LOGGER.log(Level.WARN, "Decryption failed, returning original text (legacy support?)", ex);
            return encryptedText;
        } catch (Exception ex) {
            LOGGER.log(Level.ERROR, "Error decrypting data", ex);
            // In case of other errors, we might want to return null or original text
            // Returning original text is safer for legacy migration if error is related to format
            return encryptedText;
        }
    }
}
