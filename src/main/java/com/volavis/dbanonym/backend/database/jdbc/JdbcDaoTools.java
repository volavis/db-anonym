package com.volavis.dbanonym.backend.database.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Useful methods that get used in the JdbcDao class.
 */
public final class JdbcDaoTools {

    /**
     * Constructor.
     */
    private JdbcDaoTools() {
        throw new UnsupportedOperationException();
    }

    /**
     * Retrieves an integer from a ResultSet. Instead of returning 0 when the database value is null the method returns null!
     * @param rs ResultSet on which we call getInt().
     * @param colIndex Index of the column (Always starts at 1).
     * @return Integer or null.
     */
    public static Integer getInteger(ResultSet rs, int colIndex) throws SQLException {
        int value = rs.getInt(colIndex);
        return rs.wasNull() ? null : value;
    }
}