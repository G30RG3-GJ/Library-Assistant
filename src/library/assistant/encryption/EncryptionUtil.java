package library.assistant.encryption;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.PushbackInputStream;
import java.io.Reader;
import java.io.Writer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EncryptionUtil {

    private final static Logger LOGGER = LogManager.getLogger(EncryptionUtil.class.getName());
    private static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5PADDING";
    private static final String SECRET_KEY_SPEC = "AES";
    private static final File KEY_STORE = new File("store/key.spec");
    private static final Lock LOCK = new ReentrantLock(true);

    public static String encrypt(String plainText) {
        LOCK.tryLock();
        try {
            CipherSpec spec = getCipherSpec();
            if (spec == null || !spec.isValid()) {
                throw new RuntimeException("Cant load encryption");
            }
            return encrypt(spec.getKey(), spec.getIV(), plainText);
        } catch (Exception ex) {
            LOGGER.log(Level.ERROR, "Encryption failure", ex);
        } finally {
            LOCK.unlock();
        }
        return null;
    }

    public static String decrypt(String cipherText) {
        LOCK.lock();
        try {
            CipherSpec spec = getCipherSpec();
            if (spec == null || !spec.isValid()) {
                throw new RuntimeException("Cant load encryption");
            }
            return decrypt(spec.getKey(), spec.getIV(), cipherText);
        } catch (Exception ex) {
            LOGGER.log(Level.ERROR, "Encryption failure", ex);
        } finally {
            LOCK.unlock();
        }
        return null;
    }

    private static String encrypt(byte[] key, byte[] initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, SECRET_KEY_SPEC);
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private static String decrypt(byte[] key, byte[] initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, SECRET_KEY_SPEC);

            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static void init() throws Exception {
        CipherSpec spec = getCipherSpec();
        if (spec == null || !spec.isValid()) {
            LOGGER.log(Level.INFO, "Preparing new cipher setup");
            byte[] key = generateSecureKey();
            byte[] initVector = prepareIV();
            spec = new CipherSpec(key, initVector);
            writeKey(spec);
        } else {
            LOGGER.log(Level.INFO, "Encryption params are loaded.");
        }
    }

    private static byte[] generateSecureKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(SECRET_KEY_SPEC);
        keyGen.init(128);
        SecretKey secretKey = keyGen.generateKey();
        byte[] data = secretKey.getEncoded();
        return data;
    }

    private static byte[] prepareIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    private static void writeKey(CipherSpec spec) throws Exception {
        File parent = KEY_STORE.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        if (KEY_STORE.exists()) {
            LOGGER.log(Level.INFO, "Clearing existing encryption info");
            KEY_STORE.delete();
        }
        try (Writer writer = new FileWriter(KEY_STORE)) {
            new Gson().toJson(spec, writer);
        }
        if (KEY_STORE.exists()) {
            LOGGER.log(Level.INFO, "Added new encryption setup");
        }
    }

    private static CipherSpec getCipherSpec() throws Exception {
        CipherSpec spec = null;
        boolean isLegacy = false;

        if (KEY_STORE.exists()) {
            try (PushbackInputStream pis = new PushbackInputStream(new FileInputStream(KEY_STORE), 2)) {
                byte[] header = new byte[2];
                int len = pis.read(header);
                if (len < 2) {
                    return null;
                }
                pis.unread(header, 0, len);

                if (header[0] == (byte) 0xAC && header[1] == (byte) 0xED) {
                    // Legacy
                    try (ObjectInputStream in = new LookAheadObjectInputStream(pis)) {
                        spec = (CipherSpec) in.readObject();
                        isLegacy = true;
                    }
                } else {
                    // Assume JSON
                    try (Reader reader = new InputStreamReader(pis)) {
                        spec = new Gson().fromJson(reader, CipherSpec.class);
                    }
                }
            }
        }

        if (isLegacy && spec != null) {
            writeKey(spec); // Migrate
        }

        return spec;
    }

    private static class LookAheadObjectInputStream extends ObjectInputStream {

        public LookAheadObjectInputStream(InputStream inputStream) throws IOException {
            super(inputStream);
        }

        @Override
        protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            if (!desc.getName().equals(CipherSpec.class.getName()) && !desc.getName().equals("[B")) {
                throw new InvalidClassException("Unauthorized deserialization attempt", desc.getName());
            }
            return super.resolveClass(desc);
        }
    }
}
