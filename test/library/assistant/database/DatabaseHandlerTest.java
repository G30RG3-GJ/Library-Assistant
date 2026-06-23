package library.assistant.database;

import java.sql.Connection;
import java.sql.ResultSet;
import org.junit.Test;
import static org.junit.Assert.*;

public class DatabaseHandlerTest {

    @Test
    public void testGetInstance() {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        assertNotNull("DatabaseHandler instance should not be null", handler);
    }

    @Test
    public void testGetConnection() {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        Connection conn = handler.getConnection();
        assertNotNull("Connection should not be null", conn);
    }

    @Test
    public void testExecQuery() throws Exception {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        // Derby dummy table query
        ResultSet rs = handler.execQuery("SELECT 1 FROM SYSIBM.SYSDUMMY1");
        assertNotNull("ResultSet should not be null", rs);
        assertTrue("ResultSet should have at least one row", rs.next());
        assertEquals(1, rs.getInt(1));
    }

    @Test
    public void testTablesExist() throws Exception {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        ResultSet rs = handler.execQuery("SELECT COUNT(*) FROM BOOK");
        assertNotNull("BOOK table query result should not be null", rs);
        assertTrue(rs.next());
    }
}
