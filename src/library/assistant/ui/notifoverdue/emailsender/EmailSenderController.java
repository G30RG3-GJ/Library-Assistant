package library.assistant.ui.notifoverdue.emailsender;

import com.jfoenix.controls.JFXProgressBar;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import library.assistant.alert.AlertMaker;
import library.assistant.data.model.MailServerInfo;
import library.assistant.database.DataHelper;
import library.assistant.ui.notifoverdue.NotificationItem;
import library.assistant.util.LibraryAssistantUtil;

/**
 * FXML Controller class
 *
 * @author Villan
 */
public class EmailSenderController implements Initializable {

    @FXML
    private JFXProgressBar progressBar;
    @FXML
    private Text text;

    private List<NotificationItem> list;
    private StringBuilder emailText = new StringBuilder();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Scanner scanner = new Scanner(getClass().getResourceAsStream(LibraryAssistantUtil.MAIL_CONTENT_LOC));
            while (scanner.hasNext()) {
                emailText.append(scanner.nextLine()).append("\n");
            }
            System.out.println(emailText);
        } catch (Exception ex) {
            Logger.getLogger(EmailSenderController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setNotifRequestData(List<NotificationItem> list) {
        this.list = list;
    }

    public Stage getStage() {
        return (Stage) progressBar.getScene().getWindow();
    }

    public void start() {
        if (emailText == null || emailText.toString().isEmpty()) {
            AlertMaker.showErrorMessage("Failed", "Failed to parse email format");
            getStage().close();
        }
        MailServerInfo mailServerInfo = DataHelper.loadMailServerInfo();
        EmailTaskRunner runner = new EmailTaskRunner(list, mailServerInfo, emailText.toString(),
                (count, total) -> {
                    Platform.runLater(() -> {
                        text.setText(String.format("Notifying %d/%d", count, total));
                        progressBar.setProgress((double) count / (double) total);
                    });
                },
                (success) -> {
                    Platform.runLater(() -> {
                        text.setText("Process Completed!");
                        progressBar.setProgress(1);
                    });
                }
        );
        runner.start();
    }

}
