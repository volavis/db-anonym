package com.volavis.dbanonym.backend.database.rdbms;

/**
 * Enum for the supported rdbms.
 */
public enum RdbmsID {
    MYSQL("MySQL"),
    ORACLE("Oracle"),
    MSSQL("MS SQL Server");

    private final String name;

    /**
     * Constructor.
     * @param name Readable RDBMS name.
     */
    RdbmsID(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}