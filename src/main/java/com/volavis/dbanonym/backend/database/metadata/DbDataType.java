package com.volavis.dbanonym.backend.database.metadata;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Stores the database data type for an attribute. Only used for extracting metadata!
 */
public class DbDataType {

    private String columnName;
    private String dbDataType;

    /**
     * Constructor.
     * @param columnName Name of the column.
     * @param dbDataType Database data type of the column.
     */
    public DbDataType(String columnName, String dbDataType) {
        this.columnName = columnName;
        this.dbDataType = dbDataType;
    }

    /**
     * Constructor.
     * @param resultSetMetaData Holds information about the types/ properties of the columns in a ResultSet object.
     * @param column Index of current column.
     */
    public DbDataType(ResultSetMetaData resultSetMetaData, int column) throws SQLException {
        this.columnName = resultSetMetaData.getColumnName(column);
        this.dbDataType = resultSetMetaData.getColumnTypeName(column);
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDbDataType() {
        return dbDataType;
    }

    public void setDbDataType(String dbDataType) {
        this.dbDataType = dbDataType;
    }
}