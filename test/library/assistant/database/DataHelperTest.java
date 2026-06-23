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

    @Test
    public void testIssueBookSuccess() throws SQLException {
        // Arrange
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockInsertStmt = mock(PreparedStatement.class);
        PreparedStatement mockUpdateStmt = mock(PreparedStatement.class);

        when(mockConn.prepareStatement(contains("INSERT INTO ISSUE"))).thenReturn(mockInsertStmt);
        when(mockConn.prepareStatement(contains("UPDATE BOOK"))).thenReturn(mockUpdateStmt);

        when(mockInsertStmt.executeUpdate()).thenReturn(1);
        when(mockUpdateStmt.executeUpdate()).thenReturn(1);

        // Act
        boolean result = DataHelper.issueBook("M001", "B001", mockConn);

        // Assert
        assertTrue(result);
        verify(mockConn).setAutoCommit(false);
        verify(mockInsertStmt).setString(1, "M001");
        verify(mockInsertStmt).setString(2, "B001");
        verify(mockUpdateStmt).setString(1, "B001");
        verify(mockConn).commit();
        verify(mockConn).setAutoCommit(true);
    }

    @Test
    public void testIssueBookFailure() throws SQLException {
        // Arrange
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockInsertStmt = mock(PreparedStatement.class);

        when(mockConn.prepareStatement(contains("INSERT INTO ISSUE"))).thenReturn(mockInsertStmt);
        when(mockInsertStmt.executeUpdate()).thenReturn(0); // Insert fails

        // Act
        boolean result = DataHelper.issueBook("M001", "B001", mockConn);

        // Assert
        assertFalse(result);
        verify(mockConn).setAutoCommit(false);
        verify(mockConn).rollback();
        verify(mockConn).setAutoCommit(true);
    }

    @Test
    public void testIssueBookPartialFailure() throws SQLException {
        // Arrange
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockInsertStmt = mock(PreparedStatement.class);
        PreparedStatement mockUpdateStmt = mock(PreparedStatement.class);

        when(mockConn.prepareStatement(contains("INSERT INTO ISSUE"))).thenReturn(mockInsertStmt);
        when(mockConn.prepareStatement(contains("UPDATE BOOK"))).thenReturn(mockUpdateStmt);

        when(mockInsertStmt.executeUpdate()).thenReturn(1); // Insert succeeds
        when(mockUpdateStmt.executeUpdate()).thenReturn(0); // Update fails

        // Act
        boolean result = DataHelper.issueBook("M001", "B001", mockConn);

        // Assert
        assertFalse(result);
        verify(mockConn).setAutoCommit(false);
        verify(mockConn).rollback();
        verify(mockConn).setAutoCommit(true);
    }
}
