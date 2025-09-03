package com.volavis.dbanonym.backend.database.rdbms.mysql;

import com.volavis.dbanonym.backend.database.metadata.DefaultMetadataExtractor;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.backend.database.metadata.Metadata;
import com.volavis.dbanonym.backend.database.rdbms.DatabaseVersion;
import com.volavis.dbanonym.backend.database.jdbc.JdbcDao;

import java.util.List;

/**
 * MySQL metadata extractor.
 */
public class MysqlMetadataExtractor extends DefaultMetadataExtractor {

    /**
     * Constructor.
     * @param dao JDBC DAO.
     * @param dbVersion The database version.
     * @param metadata The database's metadata, e.g. database name, entities, attributes, etc.
     */
    public MysqlMetadataExtractor(JdbcDao dao, DatabaseVersion dbVersion, Metadata metadata) {
        super(dao, dbVersion, metadata);
    }

    /**
     * MySQL does not know schemas. Only the table names will be queried!
     */
    @Override
    protected void extractEntities() {
        List<String> entityNamesList = dbVersion.queryEntityNames(dao);
        for(String entityName : entityNamesList) {
            metadata.addEntity(new Entity(null, entityName));
        }
    }
}