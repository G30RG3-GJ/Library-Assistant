package library.assistant.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SqlHelperTest {

    private Connection conn;

    @Before
    public void setUp() throws Exception {
        // Use in-memory database for testing
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
        conn = DriverManager.getConnection("jdbc:derby:memory:testdb;create=true");
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE TEST_TABLE (ID INT PRIMARY KEY, NAME VARCHAR(100))");
        }
    }

    @After
    public void tearDown() throws Exception {
        try {
            DriverManager.getConnection("jdbc:derby:memory:testdb;drop=true");
        } catch (Exception e) {
            // Derby throws exception on successfull drop
        }
    }

    @Test
    public void testExecUpdate_Insert() throws Exception {
        String insert = "INSERT INTO TEST_TABLE (ID, NAME) VALUES (?, ?)";
        boolean result = SqlHelper.execUpdate(conn, insert, 1, "Test Name");
        assertTrue("Insert should return true", result);

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT NAME FROM TEST_TABLE WHERE ID = 1");
            assertTrue("Row should exist", rs.next());
            assertEquals("Name should match", "Test Name", rs.getString("NAME"));
        }
    }

    @Test
    public void testExecUpdate_Update() throws Exception {
        // Insert first
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("INSERT INTO TEST_TABLE (ID, NAME) VALUES (2, 'Initial Name')");
        }

        String update = "UPDATE TEST_TABLE SET NAME = ? WHERE ID = ?";
        boolean result = SqlHelper.execUpdate(conn, update, "Updated Name", 2);
        assertTrue("Update should return true", result);

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT NAME FROM TEST_TABLE WHERE ID = 2");
            assertTrue("Row should exist", rs.next());
            assertEquals("Name should be updated", "Updated Name", rs.getString("NAME"));
        }
    }

    @Test
    public void testExecUpdate_Delete() throws Exception {
        // Insert first
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("INSERT INTO TEST_TABLE (ID, NAME) VALUES (3, 'To Delete')");
        }

        String delete = "DELETE FROM TEST_TABLE WHERE ID = ?";
        boolean result = SqlHelper.execUpdate(conn, delete, 3);
        assertTrue("Delete should return true", result);

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM TEST_TABLE WHERE ID = 3");
            rs.next();
            assertEquals("Count should be 0", 0, rs.getInt(1));
        }
    }

    @Test
    public void testExecUpdate_InvalidSql() {
        String invalid = "INSERT INTO NON_EXISTENT_TABLE VALUES (?)";
        boolean result = SqlHelper.execUpdate(conn, invalid, 1);
        assertFalse("Invalid SQL should return false", result);
    }
}
