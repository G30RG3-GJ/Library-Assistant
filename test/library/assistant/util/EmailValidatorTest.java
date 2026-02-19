package library.assistant.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class EmailValidatorTest {

    @Test
    public void testValidEmails() {
        assertTrue("simple@example.com should be valid", EmailValidator.validate("simple@example.com"));
        assertTrue("very.common@example.com should be valid", EmailValidator.validate("very.common@example.com"));
        assertTrue("disposable.style.email.with+symbol@example.com should be valid", EmailValidator.validate("disposable.style.email.with+symbol@example.com"));
        assertTrue("other.email-with-hyphen@example.com should be valid", EmailValidator.validate("other.email-with-hyphen@example.com"));
        assertTrue("fully-qualified-domain@example.com should be valid", EmailValidator.validate("fully-qualified-domain@example.com"));
        assertTrue("user.name+tag+sorting@example.com should be valid", EmailValidator.validate("user.name+tag+sorting@example.com"));
        assertTrue("example-indeed@strange-example.com should be valid", EmailValidator.validate("example-indeed@strange-example.com"));
        assertTrue("admin@mail.server.com should be valid", EmailValidator.validate("admin@mail.server.com"));
    }

    @Test
    public void testInvalidEmails() {
        assertFalse("Abc.example.com should be invalid (no @)", EmailValidator.validate("Abc.example.com"));
        assertFalse("A@b@c@example.com should be invalid (multiple @)", EmailValidator.validate("A@b@c@example.com"));
        assertFalse("a\"b(c)d,e:f;g<h>i[j\\k]l@example.com should be invalid (special chars)", EmailValidator.validate("a\"b(c)d,e:f;g<h>i[j\\k]l@example.com"));
        assertFalse("just\"not\"right@example.com should be invalid (quotes)", EmailValidator.validate("just\"not\"right@example.com"));
        assertFalse("this is\"not\\allowed@example.com should be invalid (spaces)", EmailValidator.validate("this is\"not\\allowed@example.com"));

        // Regex specific constraints
        assertFalse("user@domain.c should be invalid (TLD too short)", EmailValidator.validate("user@domain.c"));
        assertFalse("user@domain.123 should be invalid (numeric TLD)", EmailValidator.validate("user@domain.123"));
        assertFalse("user@.com.my should be invalid (leading dot in domain)", EmailValidator.validate("user@.com.my"));

        // Empty string
        assertFalse("Empty string should be invalid", EmailValidator.validate(""));
    }

    @Test(expected = NullPointerException.class)
    public void testNullEmail() {
        EmailValidator.validate(null);
    }
}
