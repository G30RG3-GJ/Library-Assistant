package library.assistant.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.junit.Assert;
import org.junit.Test;

public class LibraryAssistantUtilTest {

    @Test
    public void testFormatDateTimeString() {
        // 2023-10-25 14:30:45
        Calendar calendar = new GregorianCalendar(2023, Calendar.OCTOBER, 25, 14, 30, 45);
        Date date = calendar.getTime();

        // Expected format: dd-MM-yyyy hh:mm:ss a
        String expected = "25-10-2023 02:30:45 PM";
        String actual = LibraryAssistantUtil.formatDateTimeString(date);

        Assert.assertEquals(expected, actual);
    }
}
