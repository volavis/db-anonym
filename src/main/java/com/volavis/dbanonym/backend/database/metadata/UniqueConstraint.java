package com.volavis.dbanonym.backend.database.metadata;

/**
 * Stores the unique constraint information for an attribute. Only used for extracting metadata!
 */
public class UniqueConstraint {

    private String columnName;
    private String constraintName;

    /**
     * Constructor.
     * @param columnName Name of the column.
     * @param constraintName The constraint name.
     */
    public UniqueConstraint(String columnName, String constraintName) {
        this.columnName = columnName;
        this.constraintName = constraintName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getConstraintName() {
        return constraintName;
    }

    public void setConstraintName(String constraintName) {
        this.constraintName = constraintName;
    }
}