package com.volavis.dbanonym.backend.database.rdbms;

import com.volavis.dbanonym.backend.anonymization.ValueWithPKs;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.metadata.DbDataType;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.backend.database.jdbc.JdbcDao;
import com.volavis.dbanonym.backend.database.metadata.UniqueConstraint;

import java.util.List;

/**
 * Abstract class for a database version.
 */
public abstract class DatabaseVersion {

    /**
     * Returns the VersionID.
     */
    public abstract VersionID getVersionID();

    /**
     * Maps an UnknownAttribute to a specific attribute class.
     * <P>Additionally the attribute can be changed, like e.g. the JDBC data type, precision, scale, etc.
     * @param dao JDBC DAO.
     * @param dbName Name of the database.
     * @param entity The entity the attribute belongs to.
     * @param attribute Attribute which is currently being considered.
     * @return Specific attribute.
     */
    public abstract Attribute getAttribute(JdbcDao dao, String dbName, Entity entity, Attribute attribute);

    /**
     * Returns a SQL query that will be used to extract metadata, but that does not query data!
     * @param dbName Name of the database.
     * @param entity Entity that holds the information.
     * @return SQL query.
     */
    public abstract String getEmptyResultSetQuery(String dbName, Entity entity);

    /**
     * Queries the name of the database.
     * @param jdbcDao DAO.
     * @return Database name.
     */
    public abstract String queryDatabaseName(JdbcDao jdbcDao);

    /**
     * Queries the names of the entities.
     * @param jdbcDao DAO.
     * @return List of entity names.
     */
    public abstract List<String> queryEntityNames(JdbcDao jdbcDao);

    /**
     * Queries an entity's record count (Number of values in the table).
     * @param jdbcDao DAO.
     * @param dbName Name of the database.
     * @param entity Entity to be updated.
     * @return Record count.
     */
    public abstract Long queryRecordCount(JdbcDao jdbcDao, String dbName, Entity entity);

    /**
     * Queries the database data types of an entity's attributes.
     * @param jdbcDao DAO.
     * @param dbName Name of the database.
     * @param entity Entity to be updated.
     * @return List of DbType objects (contain the data type and the attribute name).
     */
    public abstract List<DbDataType> queryDatabaseDataTypes(JdbcDao jdbcDao, String dbName, Entity entity);

    /**
     * Queries an entity's check constraints (belong to the entity, not to an attribute!).
     * @param jdbcDao DAO.
     * @param dbName Name of the database.
     * @param entity Entity to be updated.
     * @return List of check constraints.
     */
    public abstract List<String> queryCheckConstraints(JdbcDao jdbcDao, String dbName, Entity entity);

    /**
     * Queries an entity's unique constraints.
     * @param jdbcDao DAO.
     * @param dbName Name of the database.
     * @param entity Entity to be updated.
     * @return List of UniqueConstraint objects (contain the constraint name and the attribute name).
     */
    public abstract List<UniqueConstraint> queryUniqueConstraints(JdbcDao jdbcDao, String dbName, Entity entity);

    /**
     * Updates all rows with null.
     * @param jdbcDao DAO.
     * @param dbName Name of the database.
     * @param entity Entity to be updated.
     * @param attribute Attribute to be updated.
     */
    public abstract void updateAllRowsWithNull(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute);

    /**
     * Updates all rows with the attribute's default value.
     * @param jdbcDao DAO.
     * @param dbName Name of the database.
     * @param entity Entity to be updated.
     * @param attribute Attribute to be updated.
     */
    public abstract void updateAllRowsWithDefaultValue(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute);

    /**
     * Updates all rows with the same value.
     * @param jdbcDao DAO.
     * @param dbName Name of the database.
     * @param entity Entity to be updated.
     * @param attribute Attribute to be updated.
     * @param value Value used for updating.
     */
    public abstract void updateAllRowsWithSameValue(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute, Object value);

    /**
     * Batch update: Updates all rows with different values.
     * @param jdbcDao DAO.
     * @param dbName Name of the database.
     * @param batchSize Size of the batch
     * @param entity Entity to be updated.
     * @param valueAttr Attribute to be updated.
     * @param pkList List of primary keys.
     * @param valueList List of ValueWithPKs objects. Contain the values and the pk values.
     */
    public abstract void batchUpdateAllRowsDifferentValues(JdbcDao jdbcDao, String dbName, int batchSize, Entity entity, Attribute valueAttr, List<Attribute> pkList, List<ValueWithPKs> valueList);

    /**
     * Queries an entity's primary keys.
     * @param jdbcDao DAO.
     * @param dbName Name of the database.
     * @param entity Entity that holds the information.
     * @param pkList List of primary keys.
     * @param valueList List of ValueWithPKs objects. Contain the values and the pk values.
     */
    public abstract void queryEntityPKs(JdbcDao jdbcDao, String dbName, Entity entity, List<Attribute> pkList, List<ValueWithPKs> valueList);

    /**
     * Queries the additional metadata length (String = Char length, Byte = Byte length, etc.).
     * @param jdbcDao DAO.
     * @param dbName Name of the database.
     * @param entity Entity that holds the information.
     * @param attribute Attribute that holds the information.
     * @return List of lengths.
     */
    public abstract List<Object> queryLengths(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute);

    /**
     * Queries the additional decimal metadata used for the anonymization (length, pre- and post decimal point lengths).
     * @param jdbcDao DAO.
     * @param dbName Name of the database.
     * @param entity Entity that holds the information.
     * @param attribute Attribute that holds the information.
     * @return List of DecimalData.
     */
    public abstract List<Object> queryDecimalData(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute);

    /**
     * Queries the additional metadata sign (minus, plus).
     * @param jdbcDao DAO.
     * @param dbName Name of the database.
     * @param entity Entity that holds the information.
     * @param attribute Attribute that holds the information.
     * @return List of Booleans (true = plus, false = minus, null = null).
     */
    public abstract List<Object> querySign(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute);

    /**
     * Drops a unique constraint.
     * @param jdbcDao DAO.
     * @param dbName Name of the database.
     * @param entity Entity to be updated.
     * @param attribute Attribute to be updated.
     */
    public abstract void dropUniqueConstraint(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute);

    /**
     * Creates a unique constraint.
     * @param jdbcDao DAO.
     * @param dbName Name of the database.
     * @param entity Entity to be updated.
     * @param attribute Attribute to be updated.
     */
    public abstract void createUniqueConstraint(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute);
}