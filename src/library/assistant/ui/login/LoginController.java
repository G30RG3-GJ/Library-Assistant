package library.assistant.ui.login;

import com.jfoenix.controls.JFXButton;
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
    @FXML
    private JFXButton loginButton;

    Preferences preference;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        preference = Preferences.getPreferences();
        if (preference.getUsername() == null || preference.getUsername().isEmpty()) {
            loginButton.setText("Create Account");
        }
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String uname = StringUtils.trimToEmpty(username.getText());
        String pword = password.getText();

        if (preference.getUsername() == null || preference.getUsername().isEmpty()) {
            if (uname.isEmpty() || pword.isEmpty()) {
                username.getStyleClass().add("wrong-credentials");
                password.getStyleClass().add("wrong-credentials");
                return;
            }

            preference.setUsername(uname);
            preference.setPassword(DigestUtils.shaHex(pword));
            Preferences.writePreferenceToFile(preference);

            closeStage();
            loadMain();
            LOGGER.log(Level.INFO, "Admin account created for user {}", uname);
        } else {
            String pwordHex = DigestUtils.shaHex(pword);

            if (uname.equals(preference.getUsername()) && pwordHex.equals(preference.getPassword())) {
                closeStage();
                loadMain();
                LOGGER.log(Level.INFO, "User successfully logged in {}", uname);
            } else {
                username.getStyleClass().add("wrong-credentials");
                password.getStyleClass().add("wrong-credentials");
            }
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
        } catch (IOException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
    }

}
