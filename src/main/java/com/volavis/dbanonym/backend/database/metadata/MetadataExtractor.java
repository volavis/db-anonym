package com.volavis.dbanonym.backend.database.metadata;

import com.volavis.dbanonym.backend.database.jdbc.JdbcDao;
import com.volavis.dbanonym.backend.database.rdbms.DatabaseVersion;
import org.springframework.jdbc.support.MetaDataAccessException;

/**
 * Extracts the database's metadata.
 */
public abstract class MetadataExtractor {

    protected final JdbcDao dao;
    protected final DatabaseVersion dbVersion;
    protected final Metadata metadata;

    /**
     * Constructor.
     * @param dao JDBC DAO.
     * @param dbVersion The database version.
     * @param metadata The database's metadata, e.g. database name, entities, attributes, etc.
     */
    public MetadataExtractor(JdbcDao dao, DatabaseVersion dbVersion, Metadata metadata) {
        this.dao = dao;
        this.dbVersion = dbVersion;
        this.metadata = metadata;
    }

    /**
     * Extracts all the needed metadata.
     */
    public abstract void extractMetadata() throws MetaDataAccessException;
}