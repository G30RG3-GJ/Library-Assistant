package library.assistant.database;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseHandlerTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private Statement mockStatement;

    @Mock
    private ResultSet mockResultSet;

    @Test
    public void testExecQueryClosesStatementOnCompletion() throws Exception {
        // Setup mock behavior
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

        // Get instance (it's a singleton)
        // This will trigger static initialization. Ensure libs are in classpath.
        DatabaseHandler handler = DatabaseHandler.getInstance();

        // Inject mock connection via reflection
        Field connField = DatabaseHandler.class.getDeclaredField("conn");
        connField.setAccessible(true);
        connField.set(null, mockConnection); // static field

        // Execute query
        ResultSet rs = handler.execQuery("SELECT * FROM TEST");

        // Verify statement creation and execution
        verify(mockConnection).createStatement();
        verify(mockStatement).executeQuery("SELECT * FROM TEST");

        // Verify closeOnCompletion is called
        verify(mockStatement).closeOnCompletion();
    }

    @Test
    public void testExecActionClosesStatement() throws Exception {
        // Setup mock behavior
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.execute(anyString())).thenReturn(true);

        // Get instance
        DatabaseHandler handler = DatabaseHandler.getInstance();

        // Inject mock connection via reflection
        Field connField = DatabaseHandler.class.getDeclaredField("conn");
        connField.setAccessible(true);
        connField.set(null, mockConnection);

        // Execute action
        handler.execAction("UPDATE TEST SET A=1");

        // Verify statement creation and execution
        verify(mockConnection).createStatement();
        verify(mockStatement).execute("UPDATE TEST SET A=1");

        // Verify close is called
        verify(mockStatement).close();
    }
}
