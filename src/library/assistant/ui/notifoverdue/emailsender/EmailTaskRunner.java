package library.assistant.ui.notifoverdue.emailsender;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import library.assistant.data.callback.GenericCallback;
import library.assistant.data.model.MailServerInfo;
import library.assistant.email.EmailUtil;
import library.assistant.ui.notifoverdue.NotificationItem;
import library.assistant.ui.settings.Preferences;
import library.assistant.util.LibraryAssistantUtil;

public class EmailTaskRunner implements GenericCallback {

    private final List<NotificationItem> list;
    private final MailServerInfo mailServerInfo;
    private final String emailText;
    private final BiConsumer<Integer, Integer> progressCallback;
    private final Consumer<Boolean> completionCallback;
    private final AtomicInteger count = new AtomicInteger(0);
    private final int total;

    public EmailTaskRunner(List<NotificationItem> list, MailServerInfo mailServerInfo, String emailText,
                           BiConsumer<Integer, Integer> progressCallback, Consumer<Boolean> completionCallback) {
        this.list = list;
        this.mailServerInfo = mailServerInfo;
        this.emailText = emailText;
        this.progressCallback = progressCallback;
        this.completionCallback = completionCallback;
        this.total = list.size();
    }

    public void start() {
        if (list.isEmpty()) {
            if (completionCallback != null) {
                completionCallback.accept(true);
            }
            return;
        }

        for (NotificationItem item : list) {
            String reportDate = LibraryAssistantUtil.getDateString(new Date());
            String bookName = item.getBookName();
            String issueDate = item.getIssueDate();
            Integer daysUsed = item.getDayCount();
            String finePerDay = String.valueOf(Preferences.getPreferences().getFinePerDay());
            String amount = item.getFineAmount();
            String emailContent = String.format(emailText, reportDate, bookName, issueDate, daysUsed, finePerDay, amount);

            EmailUtil.sendMail(mailServerInfo, item.getMemberEmail(), emailContent, "Library Assistant Overdue Notification", this);
        }
    }

    @Override
    public Object taskCompleted(Object val) {
        int current = count.incrementAndGet();
        if (progressCallback != null) {
            progressCallback.accept(current, total);
        }
        if (current == total && completionCallback != null) {
            completionCallback.accept(true);
        }
        return null;
    }
}
