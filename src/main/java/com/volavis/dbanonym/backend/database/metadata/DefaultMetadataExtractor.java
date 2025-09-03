package com.volavis.dbanonym.backend.database.metadata;

import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.backend.database.attributes.rdbmsunspecific.UnknownAttribute;
import com.volavis.dbanonym.backend.database.jdbc.JdbcDao;
import com.volavis.dbanonym.backend.database.JavaDataType;
import com.volavis.dbanonym.backend.database.rdbms.DatabaseVersion;
import org.springframework.jdbc.support.MetaDataAccessException;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Default metadata extractor.
 * <P>Extracts the database's metadata.
 */
public class DefaultMetadataExtractor extends MetadataExtractor {

    /**
     * Constructor.
     * @param dao JDBC DAO.
     * @param dbVersion The database version.
     * @param metadata The database's metadata, e.g. database name, entities, attributes, etc.
     */
    public DefaultMetadataExtractor(JdbcDao dao, DatabaseVersion dbVersion, Metadata metadata) {
        super(dao, dbVersion, metadata);
    }

    @Override
    public void extractMetadata() throws MetaDataAccessException {

        extractDatabaseName();
        extractEntities();

        for(Entity entity : metadata.getEntities().values()) {
            extractEntityRecordCount(entity);
            extractEntityAttributes(entity);
            extractDatabaseDataTypes(entity);
            extractEntityDefaultValues(entity);
            extractEntityCheckConstraints(entity);
            extractEntityPrimaryKeys(entity);
            extractEntityForeignKeys(entity);
            extractEntityUniqueConstraints(entity);

            performAttributeMapping(entity);
        }
    }

    /**
     * Queries the name of the database.
     */
    protected void extractDatabaseName() {
        metadata.setDatabaseName(dbVersion.queryDatabaseName(dao));
    }

    /**
     * Queries the names of the entities and creates the entities.
     * <P> Additionally for some RDBMS the schema name will queried ("schema.entity").
     */
    protected void extractEntities() {
        List<String> entityNamesList = dbVersion.queryEntityNames(dao);
        for(String entityName : entityNamesList) {
            String[] parts = entityName.split("\\.");
            metadata.addEntity(new Entity(parts[0], parts[1]));
        }
    }

    /**
     * Queries an entity's record count.
     * @param entity The entity that will be updated.
     */
    protected void extractEntityRecordCount(Entity entity) {
        entity.setRecordCount(dbVersion.queryRecordCount(dao, metadata.getDatabaseName(), entity));
    }

    /**
     * Queries all the attributes of an entity.
     * @param entity The entity that will be updated.
     */
    protected void extractEntityAttributes(Entity entity) {
        List<Attribute> attributesList = dao.queryAttributes(dbVersion.getEmptyResultSetQuery(metadata.getDatabaseName(), entity));
        entity.addAttributeList(attributesList);
    }

    /**
     * Queries the database data types of an entity's attributes.
     * @param entity The entity that will be updated.
     */
    protected void extractDatabaseDataTypes(Entity entity) {
        List<DbDataType> dbDataTypesList = dbVersion.queryDatabaseDataTypes(dao, metadata.getDatabaseName(), entity);
        for(DbDataType dbDataType : dbDataTypesList) {
            entity.getAttributes().get(dbDataType.getColumnName()).setDatabaseDataType(dbDataType.getDbDataType());
        }
    }

    /**
     * Queries the default values of an entity's attributes.
     * @param entity The entity that will be updated.
     */
    protected void extractEntityDefaultValues(Entity entity) throws MetaDataAccessException {
        List<DefaultValue> defaultValuesList = dao.queryDefaultValues(entity.getName());
        if(defaultValuesList != null) {
            for(DefaultValue defaultValue : defaultValuesList) {
                entity.getAttributes().get(defaultValue.getColumnName()).setDefaultValue(defaultValue.getDefaultValue());
            }
        }
    }

    /**
     * Queries an entity's check constraints.
     * @param entity The entity that will be updated.
     */
    protected void extractEntityCheckConstraints(Entity entity) {
        List<String> checkConstraintsList = dbVersion.queryCheckConstraints(dao, metadata.getDatabaseName(), entity);
        if(checkConstraintsList != null) {
            for(String checkConstraint : checkConstraintsList) {
                entity.addCheckConstraint(checkConstraint);
            }
            assignCheckConstraintsToAttributes(entity);
        }
    }

    /**
     * Connects the entity's check constraints with its attributes.
     * @param entity The entity that will be updated.
     */
    protected void assignCheckConstraintsToAttributes(Entity entity) {
        for(String constraint : entity.getCheckConstraints()) {
            for(Attribute attribute : entity.getAttributes().values()) {
                if(Pattern.compile(Pattern.quote(attribute.getName()), Pattern.CASE_INSENSITIVE).matcher(constraint).find()) {
                    attribute.addPossibleCheckConstraint(constraint);
                }
            }
        }
    }

    /**
     * Queries an entity's primary keys.
     * @param entity The entity that will be updated.
     */
    protected void extractEntityPrimaryKeys(Entity entity) throws MetaDataAccessException {
        List<String> primaryKeysList = dao.queryPrimaryKeys(entity.getName());
        for(String attributeName : primaryKeysList) {
            Attribute tmpAttribute = entity.getAttributes().get(attributeName);
            tmpAttribute.setPK(true);
            tmpAttribute.setUnique(true);
        }
    }

    /**
     * Queries an entity's foreign keys.
     * @param entity The entity that will be updated.
     */
    protected void extractEntityForeignKeys(Entity entity) throws MetaDataAccessException {
        List<ForeignKey> foreignKeysList = dao.queryForeignKeys(entity.getName());
        for(ForeignKey foreignKey : foreignKeysList) {
            entity.getAttributes().get(foreignKey.getColumnName()).setForeignKey(foreignKey);
        }
    }

    /**
     * Queries an entity's unique constraints.
     * @param entity The entity that will be updated.
     */
    protected void extractEntityUniqueConstraints(Entity entity) {
        List<UniqueConstraint> uniqueConstraintsList = dbVersion.queryUniqueConstraints(dao, metadata.getDatabaseName(), entity);
        if(uniqueConstraintsList != null) {
            for(UniqueConstraint uniqueConstraint : uniqueConstraintsList) {
                String columnName = uniqueConstraint.getColumnName();
                String constraintName = uniqueConstraint.getConstraintName();
                entity.getAttributes().get(columnName).setUnique(true);
                entity.getAttributes().get(columnName).setUniqueConstraintName(constraintName);
            }
        }
    }

    /**
     * Maps an UnknownAttribute to a specific attribute class.
     * <P>Additionally the attribute can be changed, like e.g. the JDBC data type, precision, scale, etc.
     * @param entity The entity that will be updated.
     */
    protected void performAttributeMapping(Entity entity) {
        for (Map.Entry<String, Attribute> entry : entity.getAttributes().entrySet()) {

            Attribute newAttribute = dbVersion.getAttribute(dao, metadata.getDatabaseName(), entity, entry.getValue());

            // Use fallback from JDBC driver
            if(newAttribute instanceof UnknownAttribute) {
                newAttribute.setUseJavaDataTypeFallback(true);
                newAttribute = JavaDataType.getAttributeFromClassName(newAttribute);
            }

            entry.setValue(newAttribute);
        }
    }
}