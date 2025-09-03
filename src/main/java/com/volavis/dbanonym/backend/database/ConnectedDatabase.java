package com.volavis.dbanonym.backend.database;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import com.volavis.dbanonym.backend.database.metadata.Metadata;
import com.volavis.dbanonym.backend.database.metadata.MetadataExtractor;
import com.volavis.dbanonym.backend.database.rdbms.DatabaseVersion;
import com.volavis.dbanonym.backend.database.jdbc.ConnectionSettings;
import com.volavis.dbanonym.backend.database.jdbc.JdbcDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.MetaDataAccessException;

/**
 * ConnectedDatabase contains all the information related to the database the user connected to.
 * <P> It saves the JdbcDAO, the connection settings, the rdbms information and the database metadata.
 */
@SpringComponent
@VaadinSessionScope
public class ConnectedDatabase {

    private final JdbcDao dao;
    private ConnectionSettings connectionSettings;
    private DatabaseVersion dbVersion;
    private Metadata metadata;

    /**
     * Constructor.
     * @param dao JDBC DAO. (autowired)
     */
    @Autowired
    public ConnectedDatabase(JdbcDao dao) {
        this.dao = dao;
    }

    /**
     * Changes the database connection, the rdbms and extracts the metadata.
     * @param settings New database connection settings entered by the user.
     */
    public void changeConnection(final ConnectionSettings settings) throws MetaDataAccessException {

        connectionSettings = settings;
        dao.changeDataSource(connectionSettings);
        dbVersion = connectionSettings.getVersion().createDatabaseVersion();

        metadata = new Metadata();
        MetadataExtractor metadataExtractor = connectionSettings.getVersion().createMetadataExtractor(dao, dbVersion, metadata);
        metadataExtractor.extractMetadata();
    }

    // Getter
    public JdbcDao getDao() {
        return dao;
    }
    public ConnectionSettings getConnectionSettings() {
        return connectionSettings;
    }
    public DatabaseVersion getDatabaseVersion() {
        return dbVersion;
    }
    public Metadata getMetadata() {
        return metadata;
    }
}