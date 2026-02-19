package library.assistant.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class DatabaseHandlerExecTest {

    private static DatabaseHandler handler;

    @BeforeClass
    public static void setUpClass() {
        handler = DatabaseHandler.getInstance();
    }

    @Test
    public void testExecActionWithParams() {
        String bookID = "TestBook_" + System.currentTimeMillis();
        String title = "Test Title";
        String author = "Test Author";
        String publisher = "Test Publisher";
        Boolean isAvail = true;

        String insertQuery = "INSERT INTO BOOK(id,title,author,publisher,isAvail) VALUES(?,?,?,?,?)";
        boolean result = handler.execAction(insertQuery, bookID, title, author, publisher, isAvail);
        assertTrue("Insert should succeed", result);

        String selectQuery = "SELECT * FROM BOOK WHERE id = ?";
        ResultSet rs = handler.execQuery(selectQuery, bookID);
        try {
            assertTrue("Should find the book", rs.next());
            assertEquals(title, rs.getString("title"));
        } catch (SQLException e) {
            fail(e.getMessage());
        }

        handler.execAction("DELETE FROM BOOK WHERE id = ?", bookID);
    }

    @Test
    public void testSqlInjectionPrevention() {
        String bookID = "InjBook_" + System.currentTimeMillis();
        String title = "Inj Title";
        String author = "Inj Author";
        String publisher = "Inj Publisher";
        Boolean isAvail = true;

        handler.execAction("INSERT INTO BOOK(id,title,author,publisher,isAvail) VALUES(?,?,?,?,?)",
                bookID, title, author, publisher, isAvail);

        String payload = "' OR '1'='1";
        String deleteQuery = "DELETE FROM BOOK WHERE id = ?";

        // Attempt deletion with payload
        handler.execAction(deleteQuery, payload);

        ResultSet rs = handler.execQuery("SELECT * FROM BOOK WHERE id = ?", bookID);
        try {
            assertTrue("Book should still exist because deletion payload was treated as literal", rs.next());
        } catch (SQLException e) {
            fail(e.getMessage());
        }

        handler.execAction("DELETE FROM BOOK WHERE id = ?", bookID);
    }
}
