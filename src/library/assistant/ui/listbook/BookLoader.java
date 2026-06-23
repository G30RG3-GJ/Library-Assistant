package library.assistant.ui.listbook;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import library.assistant.data.model.Book;

public class BookLoader {

    public List<Book> loadBooks(Connection conn) throws SQLException {
        List<Book> list = new ArrayList<>();
        String qu = "SELECT * FROM BOOK";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(qu)) {
            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                String id = rs.getString("id");
                String publisher = rs.getString("publisher");
                Boolean isAvail = rs.getBoolean("isAvail");

                list.add(new Book(id, title, author, publisher, isAvail));
            }
        }
        return list;
    }
}
