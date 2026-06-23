package library.assistant.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SqlHelper {

    private final static Logger LOGGER = LogManager.getLogger(SqlHelper.class.getName());

    public static boolean execUpdate(Connection conn, String query, Object... params) {
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.ERROR, "Exception at execUpdate", ex);
            return false;
        }
    }
}
