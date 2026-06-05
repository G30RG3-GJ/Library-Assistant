package library.assistant.util;

public class SecurityUtilTest {

    public static void main(String[] args) {
        try {
            testEncryptionDecryption();
            testLegacyFallback();
            System.out.println("All tests passed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void testEncryptionDecryption() {
        System.out.println("Testing Encryption/Decryption...");
        String originalPassword = "SuperSecretPassword123!";

        String encrypted = SecurityUtil.encrypt(originalPassword);
        System.out.println("Encrypted: " + encrypted);

        if (originalPassword.equals(encrypted)) {
             throw new RuntimeException("Encryption failed: Plaintext matches encrypted text");
        }

        String decrypted = SecurityUtil.decrypt(encrypted);
        System.out.println("Decrypted: " + decrypted);

        if (!originalPassword.equals(decrypted)) {
            throw new RuntimeException("Decryption failed: Expected " + originalPassword + ", got " + decrypted);
        }
        System.out.println("Encryption/Decryption test passed.");
    }

    private static void testLegacyFallback() {
        System.out.println("Testing Legacy Fallback...");
        String legacyPassword = "legacyPassword123";

        // This should fail decryption (because it's not base64 or too short) and return the original string
        String result = SecurityUtil.decrypt(legacyPassword);
        System.out.println("Legacy Decrypt Result: " + result);

        if (!legacyPassword.equals(result)) {
            throw new RuntimeException("Legacy Fallback failed: Expected " + legacyPassword + ", got " + result);
        }

        // Test with "password" which is valid Base64 ("cGFzc3dvcmQ=") but decodes to 6 bytes.
        // IV length is 12. So it should hit the length check and return original.
        String shortBase64 = "cGFzc3dvcmQ=";
        String result2 = SecurityUtil.decrypt(shortBase64);

        if (!shortBase64.equals(result2)) {
             throw new RuntimeException("Legacy Fallback (short base64) failed: Expected " + shortBase64 + ", got " + result2);
        }

        // Test with invalid Base64
        String invalidBase64 = "This is not base 64!!!";
        String result3 = SecurityUtil.decrypt(invalidBase64);
        if (!invalidBase64.equals(result3)) {
             throw new RuntimeException("Legacy Fallback (invalid base64) failed: Expected " + invalidBase64 + ", got " + result3);
        }

        System.out.println("Legacy Fallback test passed.");
    }
}
