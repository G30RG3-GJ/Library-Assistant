package library.assistant.exceptions;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExceptionUtilTest {

    private TestAppender appender;
    private Logger logger;

    private Level originalLevel;

    @Before
    public void setUp() {
        appender = new TestAppender();
        appender.start();
        logger = (Logger) LogManager.getLogger(ExceptionUtil.class);
        logger.addAppender(appender);
        originalLevel = logger.getLevel();
        logger.setLevel(Level.INFO);
    }

    @After
    public void tearDown() {
        logger.removeAppender(appender);
        logger.setLevel(originalLevel);
    }

    @Test
    public void testCreateLoggingProxy() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream realStream = new PrintStream(baos);

        PrintStream proxy = ExceptionUtil.createLoggingProxy(realStream);

        String testMessage = "Test Log Message";
        proxy.print(testMessage);

        Assert.assertEquals(1, appender.events.size());
        LogEvent event = appender.events.get(0);
        Assert.assertEquals(Level.INFO, event.getLevel());
        Assert.assertEquals(testMessage, event.getMessage().getFormattedMessage());

        // Verify original stream was NOT written to
        Assert.assertEquals("", baos.toString());

        appender.events.clear();

        proxy.println(testMessage);
        Assert.assertEquals(1, appender.events.size());
        LogEvent event2 = appender.events.get(0);
        Assert.assertEquals(Level.INFO, event2.getLevel());
        Assert.assertEquals(testMessage, event2.getMessage().getFormattedMessage());

        // Verify original stream still empty
        Assert.assertEquals("", baos.toString());
    }

    private static class TestAppender extends AbstractAppender {
        final List<LogEvent> events = new ArrayList<>();

        protected TestAppender() {
            super("TestAppender", null, null);
        }

        @Override
        public void append(LogEvent event) {
            events.add(event.toImmutable());
        }
    }
}
