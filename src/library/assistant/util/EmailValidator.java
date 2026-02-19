package library.assistant.util;

import java.util.regex.Pattern;

public class EmailValidator {

    private static final String REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-\\+]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    public static boolean validate(String emailID) {
        return PATTERN.matcher(emailID).matches();
    }
}
