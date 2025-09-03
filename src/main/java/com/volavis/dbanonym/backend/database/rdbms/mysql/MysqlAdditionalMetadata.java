package com.volavis.dbanonym.backend.database.rdbms.mysql;

import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.backend.database.jdbc.JdbcDao;

/**
 * MySQL interface for additional metadata queries.
 */
public interface MysqlAdditionalMetadata {

    /**
     * Queries all possible ENUM values of the attribute.
     * @param jdbcDao DAO.
     * @param dbName Name of the database.
     * @param entity Entity to be updated.
     * @param attribute Attribute to be updated.
     * @return String containing all ENUM values.
     */
    String queryEnumValues(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute);
}
