package library.assistant.database;

import java.sql.ResultSet;
import org.junit.Test;
import static org.junit.Assert.*;

public class DatabaseHandlerExecTest {

    @Test
    public void testExecActionWithParameters() throws Exception {
        DatabaseHandler handler = DatabaseHandler.getInstance();

        // Ensure table exists
        handler.execAction("CREATE TABLE TEST_TABLE (ID INT PRIMARY KEY, NAME VARCHAR(255))");

        // Clear table
        handler.execAction("DELETE FROM TEST_TABLE WHERE 1=1");

        // Insert parameterized
        boolean result = handler.execAction("INSERT INTO TEST_TABLE (ID, NAME) VALUES (?, ?)", 1, "TestName");
        assertTrue("Insert should succeed", result);

        // Select parameterized
        ResultSet rs = handler.execQuery("SELECT NAME FROM TEST_TABLE WHERE ID = ?", 1);
        assertNotNull("ResultSet should not be null", rs);
        assertTrue("ResultSet should have result", rs.next());
        assertEquals("Name should match", "TestName", rs.getString("NAME"));

        // SQL Injection attempt in Delete
        // First insert another row
        handler.execAction("INSERT INTO TEST_TABLE (ID, NAME) VALUES (?, ?)", 2, "OtherName");

        // Verify we have 2 rows
        rs = handler.execQuery("SELECT COUNT(*) FROM TEST_TABLE");
        rs.next();
        assertEquals("Should have 2 rows", 2, rs.getInt(1));

        // Attempt injection deletion
        String payload = "' OR '1'='1";
        // If this was raw string concatenation: DELETE FROM TEST_TABLE WHERE NAME = '' OR '1'='1'
        // With parameters, it looks for NAME equal to literal "' OR '1'='1"
        handler.execAction("DELETE FROM TEST_TABLE WHERE NAME = ?", payload);

        // Verify we still have 2 rows (because no name matches the payload literal)
        rs = handler.execQuery("SELECT COUNT(*) FROM TEST_TABLE");
        rs.next();
        assertEquals("Should still have 2 rows if injection failed", 2, rs.getInt(1));

        // Clean up
        handler.execAction("DROP TABLE TEST_TABLE");
    }
}
