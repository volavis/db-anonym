package com.volavis.dbanonym.backend.database.rdbms;

import com.volavis.dbanonym.backend.database.metadata.DefaultMetadataExtractor;
import com.volavis.dbanonym.backend.database.metadata.Metadata;
import com.volavis.dbanonym.backend.database.metadata.MetadataExtractor;
import com.volavis.dbanonym.backend.database.rdbms.mssql.Mssql2019;
import com.volavis.dbanonym.backend.database.rdbms.mysql.Mysql56;
import com.volavis.dbanonym.backend.database.rdbms.mysql.Mysql57;
import com.volavis.dbanonym.backend.database.rdbms.mysql.Mysql80;
import com.volavis.dbanonym.backend.database.rdbms.mysql.MysqlMetadataExtractor;
import com.volavis.dbanonym.backend.database.rdbms.oracle.Oracle19c;
import com.volavis.dbanonym.backend.database.jdbc.JdbcDao;

/**
 * Enum for the supported database versions.
 */
public enum VersionID {

    // MySQL
    MYSQL_80("8.0", RdbmsID.MYSQL, "com.mysql.cj.jdbc.Driver") {
        @Override
        public DatabaseVersion createDatabaseVersion() {
            return new Mysql80();
        }

        @Override
        public MetadataExtractor createMetadataExtractor(JdbcDao dao, DatabaseVersion dbVersion, Metadata metadata) {
            return new MysqlMetadataExtractor(dao, dbVersion, metadata);
        }
    },
    MYSQL_57("5.7", RdbmsID.MYSQL, "com.mysql.cj.jdbc.Driver") {
        @Override
        public DatabaseVersion createDatabaseVersion() {
            return new Mysql57();
        }

        @Override
        public MetadataExtractor createMetadataExtractor(JdbcDao dao, DatabaseVersion dbVersion, Metadata metadata) {
            return new MysqlMetadataExtractor(dao, dbVersion, metadata);
        }
    },
    MYSQL_56("5.6", RdbmsID.MYSQL, "com.mysql.cj.jdbc.Driver") {
        @Override
        public DatabaseVersion createDatabaseVersion() {
            return new Mysql56();
        }

        @Override
        public MetadataExtractor createMetadataExtractor(JdbcDao dao, DatabaseVersion dbVersion, Metadata metadata) {
            return new MysqlMetadataExtractor(dao, dbVersion, metadata);
        }
    },

    // Oracle
    ORACLE_19C("19c", RdbmsID.ORACLE, "oracle.jdbc.OracleDriver") {
        @Override
        public DatabaseVersion createDatabaseVersion() {
            return new Oracle19c();
        }

        @Override
        public MetadataExtractor createMetadataExtractor(JdbcDao dao, DatabaseVersion dbVersion, Metadata metadata) {
            return new DefaultMetadataExtractor(dao, dbVersion, metadata);
        }
    },

    // MSSQL
    MSSQL_2019("2019", RdbmsID.MSSQL, "com.microsoft.sqlserver.jdbc.SQLServerDriver") {
        @Override
        public DatabaseVersion createDatabaseVersion() {
            return new Mssql2019();
        }

        @Override
        public MetadataExtractor createMetadataExtractor(JdbcDao dao, DatabaseVersion dbVersion, Metadata metadata) {
            return new DefaultMetadataExtractor(dao, dbVersion, metadata);
        }
    };

    private final String name;
    private final RdbmsID rdbms;
    private final String driverClassName;

    /**
     * Creates the DatabaseVersion object.
     * @return DatabaseVersion object.
     */
    public abstract DatabaseVersion createDatabaseVersion();

    /**
     * Creates the MetadataExtractor object.
     * @param dao JDBC DAO.
     * @param dbVersion The database version.
     * @param metadata The queried metadata.
     * @return MetadataExtractor object.
     */
    public abstract MetadataExtractor createMetadataExtractor(JdbcDao dao, DatabaseVersion dbVersion, Metadata metadata);

    /**
     * Constructor.
     * @param name Readable version name.
     * @param rdbms RDBMS the version belongs to.
     * @param driverClassName Class name of the version's jdbc driver.
     */
    VersionID(String name, RdbmsID rdbms, String driverClassName) {
        this.name = name;
        this.rdbms = rdbms;
        this.driverClassName = driverClassName;
    }

    @Override
    public String toString() {
        return this.name;
    }

    // Getter and setter
    public RdbmsID getRdbms() {
        return rdbms;
    }
    public String getDriverClassName() {
        return driverClassName;
    }
}