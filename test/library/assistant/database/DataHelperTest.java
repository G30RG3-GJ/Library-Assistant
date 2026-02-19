package library.assistant.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import library.assistant.data.model.Book;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DataHelperTest {

    @Test
    public void testInsertNewBook() throws SQLException {
        // Arrange
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        Book book = new Book("B100", "Test Title", "Test Author", "Test Publisher", true);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        // Act
        boolean result = DataHelper.insertNewBook(book, mockConn);

        // Assert
        assertTrue(result);
        verify(mockStmt).setString(1, "B100");
        verify(mockStmt).setString(2, "Test Title");
        verify(mockStmt).setString(3, "Test Author");
        verify(mockStmt).setString(4, "Test Publisher");
        verify(mockStmt).setBoolean(5, true);
        verify(mockStmt).executeUpdate();
    }

    @Test
    public void testInsertNewBookFailure() throws SQLException {
        // Arrange
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        Book book = new Book("B100", "Test Title", "Test Author", "Test Publisher", true);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(0);

        // Act
        boolean result = DataHelper.insertNewBook(book, mockConn);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIssueBook() throws SQLException {
        // Arrange
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt1 = mock(PreparedStatement.class);
        PreparedStatement mockStmt2 = mock(PreparedStatement.class);
        String bookID = "B100";
        String memberID = "M100";

        when(mockConn.prepareStatement(contains("INSERT INTO ISSUE"))).thenReturn(mockStmt1);
        when(mockConn.prepareStatement(contains("UPDATE BOOK"))).thenReturn(mockStmt2);
        when(mockStmt1.executeUpdate()).thenReturn(1);
        when(mockStmt2.executeUpdate()).thenReturn(1);

        // Act
        boolean result = DataHelper.issueBook(bookID, memberID, mockConn);

        // Assert
        assertTrue(result);
        verify(mockConn).prepareStatement(contains("INSERT INTO ISSUE"));
        verify(mockStmt1).setString(1, memberID);
        verify(mockStmt1).setString(2, bookID);
        verify(mockStmt1).executeUpdate();

        verify(mockConn).prepareStatement(contains("UPDATE BOOK"));
        verify(mockStmt2).setString(1, bookID);
        verify(mockStmt2).executeUpdate();
    }

    @Test
    public void testIssueBookFailure() throws SQLException {
        // Arrange
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt1 = mock(PreparedStatement.class);
        PreparedStatement mockStmt2 = mock(PreparedStatement.class);
        String bookID = "B100";
        String memberID = "M100";

        when(mockConn.prepareStatement(contains("INSERT INTO ISSUE"))).thenReturn(mockStmt1);
        when(mockConn.prepareStatement(contains("UPDATE BOOK"))).thenReturn(mockStmt2);
        when(mockStmt1.executeUpdate()).thenReturn(1);
        when(mockStmt2.executeUpdate()).thenReturn(0); // Second update fails

        // Act
        boolean result = DataHelper.issueBook(bookID, memberID, mockConn);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIssueBookException() throws SQLException {
        // Arrange
        Connection mockConn = mock(Connection.class);
        String bookID = "B100";
        String memberID = "M100";

        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("DB Error"));

        // Act
        boolean result = DataHelper.issueBook(bookID, memberID, mockConn);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testInsertNewBookException() throws SQLException {
         // Arrange
        Connection mockConn = mock(Connection.class);
        Book book = new Book("B100", "Test Title", "Test Author", "Test Publisher", true);

        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("DB Error"));

        // Act
        boolean result = DataHelper.insertNewBook(book, mockConn);

        // Assert
        assertFalse(result);
    }
}
