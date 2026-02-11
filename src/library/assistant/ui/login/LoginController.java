package library.assistant.ui.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.ui.settings.Preferences;
import library.assistant.util.LibraryAssistantUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginController implements Initializable {

    private final static Logger LOGGER = LogManager.getLogger(LoginController.class.getName());

    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;

    Preferences preference;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        preference = Preferences.getPreferences();
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String uname = StringUtils.trimToEmpty(username.getText());
        String pword = password.getText();

        boolean passwordMatch = false;
        String storedSalt = preference.getSalt();
        String storedPassword = preference.getPassword();
        boolean migrated = false;

        if (storedSalt != null) {
            String computedHash = Preferences.hashPassword(pword, storedSalt);
            if (computedHash != null) {
                passwordMatch = computedHash.equals(storedPassword);
            }
        } else {
            // Legacy fallback (SHA-256 or SHA-1)
            String sha256 = DigestUtils.sha256Hex(pword);
            if (sha256.equals(storedPassword)) {
                passwordMatch = true;
                migrated = true;
            } else {
                String sha1 = DigestUtils.shaHex(pword);
                if (sha1.equals(storedPassword)) {
                    passwordMatch = true;
                    migrated = true;
                }
            }
        }

        if (uname.equals(preference.getUsername()) && passwordMatch) {
            if (migrated) {
                preference.setPassword(pword);
                Preferences.writePreferenceToFileWithoutAlert(preference);
            }
            closeStage();
            loadMain();
            LOGGER.log(Level.INFO, "User successfully logged in {}", uname);
        } else {
            username.getStyleClass().add("wrong-credentials");
            password.getStyleClass().add("wrong-credentials");
        }
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        System.exit(0);
    }

    private void closeStage() {
        ((Stage) username.getScene().getWindow()).close();
    }

    void loadMain() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/library/assistant/ui/main/main.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Library Assistant");
            stage.setScene(new Scene(parent));
            stage.show();
            LibraryAssistantUtil.setStageIcon(stage);
        }
        catch (IOException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
    }

}
