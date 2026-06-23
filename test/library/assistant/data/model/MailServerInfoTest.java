package library.assistant.data.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class MailServerInfoTest {

    @Test
    public void testValidate_Valid() {
        MailServerInfo info = new MailServerInfo("smtp.example.com", 587, "user@example.com", "password", true);
        assertTrue("Validation should pass for valid info", info.validate());
    }

    @Test
    public void testValidate_NullMailServer() {
        MailServerInfo info = new MailServerInfo(null, 587, "user@example.com", "password", true);
        assertFalse("Validation should fail for null mailServer", info.validate());
    }

    @Test
    public void testValidate_EmptyMailServer() {
        MailServerInfo info = new MailServerInfo("", 587, "user@example.com", "password", true);
        assertFalse("Validation should fail for empty mailServer", info.validate());
    }

    @Test
    public void testValidate_NullPort() {
        MailServerInfo info = new MailServerInfo("smtp.example.com", null, "user@example.com", "password", true);
        assertFalse("Validation should fail for null port", info.validate());
    }

    @Test
    public void testValidate_NullEmailID() {
        MailServerInfo info = new MailServerInfo("smtp.example.com", 587, null, "password", true);
        assertFalse("Validation should fail for null emailID", info.validate());
    }

    @Test
    public void testValidate_EmptyEmailID() {
        MailServerInfo info = new MailServerInfo("smtp.example.com", 587, "", "password", true);
        assertFalse("Validation should fail for empty emailID", info.validate());
    }

    @Test
    public void testValidate_EmptyPassword() {
        MailServerInfo info = new MailServerInfo("smtp.example.com", 587, "user@example.com", "", true);
        assertFalse("Validation should fail for empty password", info.validate());
    }

    @Test
    public void testValidate_NullPassword() {
        MailServerInfo info = new MailServerInfo("smtp.example.com", 587, "user@example.com", null, true);
        // Expect false, but currently might throw NPE
        assertFalse("Validation should fail for null password", info.validate());
    }
}
