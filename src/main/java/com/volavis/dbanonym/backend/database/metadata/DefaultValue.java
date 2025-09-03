package com.volavis.dbanonym.backend.database.metadata;

/**
 * Stores the default value for an attribute. Only used for extracting metadata!
 */
public class DefaultValue {

    private String columnName;
    private String defaultValue;

    /**
     * Constructor.
     * @param columnName Name of the column.
     * @param defaultValue Default value of the column.
     */
    public DefaultValue(String columnName, String defaultValue) {
        this.columnName = columnName;
        this.defaultValue = defaultValue;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}