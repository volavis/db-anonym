package com.volavis.dbanonym.backend.database.rdbms.oracle;

import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.backend.database.jdbc.JdbcDao;

/**
 * Oracle interface for additional metadata queries.
 */
public interface OracleAdditionalMetadata {

    /**
     * Used for character data types. Queries the qualifier.
     * Is the length in chars or bytes?
     * @param jdbcDao DAO.
     * @param dbName Name of the database.
     * @param entity Entity to be updated.
     * @param attribute Attribute to be updated.
     * @return Byte (B); Char (C).
     */
    String queryQualifierCharacterDataTypes(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute);
}
