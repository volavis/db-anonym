package com.volavis.dbanonym.backend.database.rdbms.mysql;

import com.volavis.dbanonym.backend.anonymization.ValueWithPKs;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.metadata.DbDataType;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.backend.database.metadata.UniqueConstraint;
import com.volavis.dbanonym.backend.database.rdbms.DatabaseVersion;
import com.volavis.dbanonym.backend.database.rdbms.VersionID;
import com.volavis.dbanonym.backend.database.jdbc.JdbcDao;

import java.sql.Types;
import java.util.List;

/**
 * MySQL 5.6.
 * <P>Info: database == catalog == schema.
 * <P>To identify an object MySQL uses: "database.object".
 */
public class Mysql56 extends DatabaseVersion implements MysqlAdditionalMetadata {

    @Override
    public VersionID getVersionID() {
        return VersionID.MYSQL_56;
    }

    @Override
    public Attribute getAttribute(JdbcDao dao, String dbName, Entity entity, Attribute attribute) {
        return MysqlMapping.getAttribute(dao, this, dbName, entity, attribute);
    }

    @Override
    public String getEmptyResultSetQuery(String dbName, Entity entity) {
        return "SELECT * FROM " + dbName + "." + entity.getName() + " WHERE 1=0;";
    }

    @Override
    public String queryDatabaseName(JdbcDao jdbcDao) {
        String sql = "SELECT DATABASE()";
        return jdbcDao.queryDatabaseName(sql);
    }

    @Override
    public List<String> queryEntityNames(JdbcDao jdbcDao) {
        String sql = "SHOW TABLES;";
        return jdbcDao.queryEntityNames(sql);
    }

    @Override
    public Long queryRecordCount(JdbcDao jdbcDao, String dbName, Entity entity) {
        String sql = "SELECT COUNT(*) FROM " + dbName + "." + entity.getName() + ";";
        return jdbcDao.queryRecordCount(sql);
    }

    @Override
    public List<DbDataType> queryDatabaseDataTypes(JdbcDao jdbcDao, String dbName, Entity entity) {
        String sql = "SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '"
                + dbName + "' AND TABLE_NAME = '" + entity.getName() + "';";
        return jdbcDao.queryDatabaseDataType(sql);
    }

    @Override
    public List<String> queryCheckConstraints(JdbcDao jdbcDao, String dbName, Entity entity) {
        // There are no check constraints prior to MySQL 8.0.16!
        return null;
    }

    @Override
    public List<UniqueConstraint> queryUniqueConstraints(JdbcDao jdbcDao, String dbName, Entity entity) {
        String sql = "SELECT COLUMN_NAME, CONSTRAINT_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE" +
                " WHERE CONSTRAINT_NAME IN (SELECT CONSTRAINT_NAME FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS" +
                " WHERE TABLE_SCHEMA = '" + dbName + "' AND TABLE_NAME = '" + entity.getName() + "'" +
                " AND CONSTRAINT_TYPE = 'UNIQUE');";
        return jdbcDao.queryUniqueConstraints(sql);
    }

    @Override
    public void updateAllRowsWithNull(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute) {
        String namedParameter = "value";
        String sql = "UPDATE " + dbName + "." + entity.getName() +
                " SET " + attribute.getName() + " = :" + namedParameter + ";";
        jdbcDao.updateAllRowsWithSameValue(sql, namedParameter, null, Types.NULL);
    }

    @Override
    public void updateAllRowsWithDefaultValue(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute) {
        String sql = "UPDATE " + dbName + "." + entity.getName() +
                " SET " + attribute.getName() + " = DEFAULT(" + attribute.getName() + ");";
        jdbcDao.updateAllRowsWithSameValue(sql);
    }

    @Override
    public void updateAllRowsWithSameValue(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute, Object value) {
        String namedParameter = "value";
        String sql = "UPDATE " + dbName + "." + entity.getName() +
                " SET " + attribute.getName() + " = :" + namedParameter + ";";
        jdbcDao.updateAllRowsWithSameValue(sql, namedParameter, value, attribute.getJdbcDataType());
    }

    @Override
    public void batchUpdateAllRowsDifferentValues(JdbcDao jdbcDao, String dbName, int batchSize,
                                                  Entity entity, Attribute valueAttr, List<Attribute> pkList,
                                                  List<ValueWithPKs> valueList) {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(dbName).append(".").append(entity.getName())
                .append(" SET ").append(valueAttr.getName()).append(" = ? WHERE ");
        for(int i = 0; i < pkList.size(); i++) {
            sql.append(pkList.get(i).getName()).append(" = ?");
            if(i < pkList.size() - 1) {
                sql.append(" AND ");
            }
        }
        sql.append(";");

        jdbcDao.batchUpdateAllRowsDifferentValues(sql.toString(), batchSize, pkList, valueAttr.getJdbcDataType(), valueList);
    }

    @Override
    public void queryEntityPKs(JdbcDao jdbcDao, String dbName, Entity entity,
                               List<Attribute> pkList, List<ValueWithPKs> valueList) {
        String aliasPrefix = "PK";
        StringBuilder sql = new StringBuilder("SELECT ");
        for(int i = 0; i < pkList.size(); i++) {
            sql.append(pkList.get(i).getName()).append(" AS ").append(aliasPrefix).append(i);
            if(i < pkList.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(" FROM ").append(dbName).append(".").append(entity.getName()).append(";");
        jdbcDao.queryEntityPKs(sql.toString(), aliasPrefix, pkList.size(), valueList);
    }

    @Override
    public List<Object> queryLengths(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute) {
        String selectPart;
        switch (attribute.getGuiID()) {
            case CHARACTERS:
                selectPart = "CHARACTER_LENGTH(" + attribute.getName() + ")";
                break;
            case INTEGER:
                selectPart = "CHARACTER_LENGTH(ABS(" + attribute.getName() + "))";
                break;
            case BINARY:
                selectPart = "LENGTH(" + attribute.getName() + ")";
                break;
            default:
                return null;
        }
        String sql = "SELECT " + selectPart + " FROM " + dbName + "." + entity.getName() + ";";
        return jdbcDao.queryLengths(sql);
    }

    @Override
    public List<Object> queryDecimalData(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute) {
        // [1] Sign
        // [2] Digit length of pre decimal point part
        // [3] Digit length of whole decimal including decimal point and minus (abs() does not "work" with float/double!)
        String selectPart = "SIGN(" + attribute.getName() + "), CHARACTER_LENGTH(ABS(TRUNCATE(" +
                attribute.getName() + ", 0))), CHARACTER_LENGTH(" + attribute.getName() + ")";
        String sql = "SELECT " + selectPart + " FROM " + dbName + "." + entity.getName() + ";";
        return jdbcDao.queryDecimalData(sql);
    }

    @Override
    public List<Object> querySign(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute) {
        String sql = "SELECT SIGN(" + attribute.getName() + ") FROM " + dbName + "." + entity.getName() + ";";
        return jdbcDao.querySign(sql);
    }

    @Override
    public String queryEnumValues(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute) {
        String sql = "SELECT COLUMN_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE " +
                "TABLE_SCHEMA = '" + dbName + "' AND TABLE_NAME = '" + entity.getName() +
                "' AND COLUMN_NAME = '" + attribute.getName() + "';";
        return jdbcDao.queryEnumValues(sql);
    }

    @Override
    public void dropUniqueConstraint(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute) {
        String sql = "ALTER TABLE " + dbName + "." + entity.getName() + " DROP INDEX " +
                attribute.getUniqueConstraintName() + ";";
        jdbcDao.executeSimpleSQLCommand(sql);
    }

    @Override
    public void createUniqueConstraint(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute) {
        String sql = "ALTER TABLE " + dbName + "." + entity.getName() + " ADD CONSTRAINT " +
                attribute.getUniqueConstraintName() + " UNIQUE (" + attribute.getName() + ");";
        jdbcDao.executeSimpleSQLCommand(sql);
    }
}