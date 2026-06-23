package library.assistant.ui.listbook;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import library.assistant.data.model.Book;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class BookLoaderTest {

    @Mock
    private Connection conn;
    @Mock
    private Statement stmt;
    @Mock
    private ResultSet rs;

    private BookLoader bookLoader;

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        bookLoader = new BookLoader();

        Mockito.when(conn.createStatement()).thenReturn(stmt);
        Mockito.when(stmt.executeQuery(Mockito.anyString())).thenReturn(rs);
    }

    @Test
    public void testLoadBooks() throws SQLException {
        // Arrange
        Mockito.when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(false); // 2 rows

        Mockito.when(rs.getString("title")).thenReturn("Book 1").thenReturn("Book 2");
        Mockito.when(rs.getString("id")).thenReturn("ID1").thenReturn("ID2");
        Mockito.when(rs.getString("author")).thenReturn("Author 1").thenReturn("Author 2");
        Mockito.when(rs.getString("publisher")).thenReturn("Pub 1").thenReturn("Pub 2");
        Mockito.when(rs.getBoolean("isAvail")).thenReturn(true).thenReturn(false);

        // Act
        List<Book> books = bookLoader.loadBooks(conn);

        // Assert
        Assert.assertEquals(2, books.size());

        Book b1 = books.get(0);
        Assert.assertEquals("Book 1", b1.getTitle());
        Assert.assertEquals("ID1", b1.getId());
        Assert.assertTrue(b1.getAvailability());

        Book b2 = books.get(1);
        Assert.assertEquals("Book 2", b2.getTitle());
        Assert.assertEquals("ID2", b2.getId());
        Assert.assertFalse(b2.getAvailability());

        Mockito.verify(conn).createStatement();
        Mockito.verify(stmt).executeQuery("SELECT * FROM BOOK");
        Mockito.verify(stmt).close();
        Mockito.verify(rs).close();
    }
}
