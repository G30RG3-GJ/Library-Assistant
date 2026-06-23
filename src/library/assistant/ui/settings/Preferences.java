package library.assistant.ui.settings;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import library.assistant.alert.AlertMaker;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class Preferences {

    public static final String CONFIG_FILE = "config.txt";
    private static final int SALT_LENGTH = 16;
    private static final int HASH_ITERATIONS = 65536;
    private static final int HASH_LENGTH = 256;

    int nDaysWithoutFine;
    float finePerDay;
    String username;
    String password;
    String salt;

    public Preferences() {
        nDaysWithoutFine = 14;
        finePerDay = 2;
        username = "admin";
        setPassword("admin");
    }

    public int getnDaysWithoutFine() {
        return nDaysWithoutFine;
    }

    public void setnDaysWithoutFine(int nDaysWithoutFine) {
        this.nDaysWithoutFine = nDaysWithoutFine;
    }

    public float getFinePerDay() {
        return finePerDay;
    }

    public void setFinePerDay(float finePerDay) {
        this.finePerDay = finePerDay;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setPassword(String password) {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[SALT_LENGTH];
        random.nextBytes(saltBytes);
        this.salt = Hex.encodeHexString(saltBytes);
        this.password = hashPassword(password, this.salt);
    }

    public static String hashPassword(String password, String salt) {
        try {
            byte[] saltBytes = Hex.decodeHex(salt);
            KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, HASH_ITERATIONS, HASH_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | org.apache.commons.codec.DecoderException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static void initConfig() {
        Writer writer = null;
        try {
            Preferences preference = new Preferences();
            Gson gson = new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preference, writer);
        } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static Preferences getPreferences() {
        Gson gson = new Gson();
        Preferences preferences = new Preferences();
        try {
            preferences = gson.fromJson(new FileReader(CONFIG_FILE), Preferences.class);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Preferences.class.getName()).info("Config file is missing. Creating new one with default config");
            initConfig();
        }
        return preferences;
    }

    public static void writePreferenceToFile(Preferences preference) {
        try {
            savePreferences(preference);
            AlertMaker.showSimpleAlert("Success", "Settings updated");
        } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            AlertMaker.showErrorMessage(ex, "Failed", "Cant save configuration file");
        }
    }

    public static void writePreferenceToFileWithoutAlert(Preferences preference) {
        try {
            savePreferences(preference);
        } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void savePreferences(Preferences preference) throws IOException {
        Writer writer = null;
        try {
            Gson gson = new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preference, writer);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}
