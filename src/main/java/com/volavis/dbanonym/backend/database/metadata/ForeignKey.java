package com.volavis.dbanonym.backend.database.metadata;

/**
 * Foreign key holds the primary key of another table's attribute.
 */
public class ForeignKey {

    private String columnName;
    private String refTableName;
    private String refColumnName;

    /**
     * Constructor.
     * @param columnName Name of the fk attribute.
     * @param refTableName Name of the pk attribute's table the fk references.
     * @param refColumnName Name of the pk attribute the fk references.
     */
    public ForeignKey(String columnName, String refTableName, String refColumnName) {
        this.columnName = columnName;
        this.refTableName = refTableName;
        this.refColumnName = refColumnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getRefTableName() {
        return refTableName;
    }

    public void setRefTableName(String refTableName) {
        this.refTableName = refTableName;
    }

    public String getRefColumnName() {
        return refColumnName;
    }

    public void setRefColumnName(String refColumnName) {
        this.refColumnName = refColumnName;
    }
}