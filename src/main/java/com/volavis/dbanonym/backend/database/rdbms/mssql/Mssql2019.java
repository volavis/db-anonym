package com.volavis.dbanonym.backend.database.rdbms.mssql;

import com.volavis.dbanonym.backend.anonymization.ValueWithPKs;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.backend.database.jdbc.JdbcDao;
import com.volavis.dbanonym.backend.database.metadata.DbDataType;
import com.volavis.dbanonym.backend.database.metadata.UniqueConstraint;
import com.volavis.dbanonym.backend.database.rdbms.DatabaseVersion;
import com.volavis.dbanonym.backend.database.rdbms.VersionID;

import java.sql.Types;
import java.util.List;

/**
 * Microsoft SQL Server 2019.
 * <P>Info: database == catalog ; schema (default dbo) != user.
 * <P>To identify an object MSSQL uses: "database.schema.object".
 */
public class Mssql2019 extends DatabaseVersion {

    @Override
    public VersionID getVersionID() {
        return VersionID.MSSQL_2019;
    }

    @Override
    public Attribute getAttribute(JdbcDao dao, String dbName, Entity entity, Attribute attribute) {
        return MssqlMapping.getAttribute(dao, this, dbName, entity, attribute);
    }

    @Override
    public String getEmptyResultSetQuery(String dbName, Entity entity) {
        return "SELECT * FROM " + dbName + "." + entity.getSchema() + "." + entity.getName() + " WHERE 1=0;";
    }

    @Override
    public String queryDatabaseName(JdbcDao jdbcDao) {
        String sql = "SELECT DB_NAME();";
        return jdbcDao.queryDatabaseName(sql);
    }

    @Override
    public List<String> queryEntityNames(JdbcDao jdbcDao) {
        String sql = "SELECT TABLE_SCHEMA, TABLE_NAME FROM INFORMATION_SCHEMA.TABLES;";
        return jdbcDao.queryEntityNamesWithSchema(sql);
    }

    @Override
    public Long queryRecordCount(JdbcDao jdbcDao, String dbName, Entity entity) {
        String sql = "SELECT COUNT(*) FROM " + dbName + "." + entity.getSchema() + "." + entity.getName() + ";";
        return jdbcDao.queryRecordCount(sql);
    }

    @Override
    public List<DbDataType> queryDatabaseDataTypes(JdbcDao jdbcDao, String dbName, Entity entity) {
        String sql = "SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS" +
                " WHERE TABLE_CATALOG = '" + dbName + "' AND TABLE_SCHEMA = '" + entity.getSchema() + "'" +
                " AND TABLE_NAME = '" + entity.getName() + "';";
        return jdbcDao.queryDatabaseDataType(sql);
    }

    @Override
    public List<String> queryCheckConstraints(JdbcDao jdbcDao, String dbName, Entity entity) {
        String sql = "SELECT C.CHECK_CLAUSE FROM INFORMATION_SCHEMA.CHECK_CONSTRAINTS AS C " +
                "JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS T ON C.CONSTRAINT_NAME = T.CONSTRAINT_NAME " +
                "WHERE C.CONSTRAINT_CATALOG = '" + dbName + "' AND C.CONSTRAINT_SCHEMA = '"
                + entity.getSchema() + "' AND T.TABLE_NAME = '" + entity.getName() + "';";
        return jdbcDao.queryCheckConstraints(sql);
    }

    @Override
    public List<UniqueConstraint> queryUniqueConstraints(JdbcDao jdbcDao, String dbName, Entity entity) {
        String sql = "SELECT COLUMN_NAME, CONSTRAINT_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE" +
                " WHERE CONSTRAINT_NAME IN (SELECT CONSTRAINT_NAME FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS" +
                " WHERE TABLE_CATALOG = '" + dbName + "' AND TABLE_SCHEMA = '" + entity.getSchema() + "'" +
                " AND TABLE_NAME = '" + entity.getName() + "' AND CONSTRAINT_TYPE = 'UNIQUE');";
        return jdbcDao.queryUniqueConstraints(sql);
    }

    @Override
    public void updateAllRowsWithNull(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute) {
        String namedParameter = "value";
        String sql = "UPDATE " + dbName + "." + entity.getSchema() + "." + entity.getName() +
                " SET " + attribute.getName() + " = :" + namedParameter + ";";
        jdbcDao.updateAllRowsWithSameValue(sql, namedParameter, null, Types.NULL);
    }

    @Override
    public void updateAllRowsWithDefaultValue(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute) {
        String sql = "UPDATE " + dbName + "." + entity.getSchema() + "." + entity.getName() +
                " SET " + attribute.getName() + " = DEFAULT;";
        jdbcDao.updateAllRowsWithSameValue(sql);
    }

    @Override
    public void updateAllRowsWithSameValue(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute, Object value) {
        String namedParameter = "value";
        String sql = "UPDATE " + dbName + "." + entity.getSchema() + "." + entity.getName() +
                " SET " + attribute.getName() + " = :" + namedParameter + ";";

        jdbcDao.updateAllRowsWithSameValue(sql, namedParameter, value, attribute.getJdbcDataType());
    }

    @Override
    public void batchUpdateAllRowsDifferentValues(JdbcDao jdbcDao, String dbName, int batchSize, Entity entity, Attribute valueAttr, List<Attribute> pkList, List<ValueWithPKs> valueList) {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(dbName).append(".").append(entity.getSchema()).append(".").append(entity.getName())
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
    public void queryEntityPKs(JdbcDao jdbcDao, String dbName, Entity entity, List<Attribute> pkList, List<ValueWithPKs> valueList) {
        String aliasPrefix = "PK";
        StringBuilder sql = new StringBuilder("SELECT ");
        for(int i = 0; i < pkList.size(); i++) {
            sql.append(pkList.get(i).getName()).append(" AS ").append(aliasPrefix).append(i);
            if(i < pkList.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(" FROM ").append(dbName).append(".").append(entity.getSchema()).append(".").append(entity.getName()).append(";");
        jdbcDao.queryEntityPKs(sql.toString(), aliasPrefix, pkList.size(), valueList);
    }

    @Override
    public List<Object> queryLengths(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute) {
        String selectPart;
        switch (attribute.getGuiID()) {
            case CHARACTERS:
                selectPart = "LEN(" + attribute.getName() + ")";
                break;
            case BINARY:
                selectPart = "DATALENGTH(" + attribute.getName() + ")";
                break;
            case INTEGER:
                selectPart = "LEN(ABS(" + attribute.getName() + "))";
                break;
            default:
                return null;
        }
        String sql = "SELECT " + selectPart + " FROM " + dbName + "." + entity.getSchema() + "." + entity.getName() + ";";
        return jdbcDao.queryLengths(sql);
    }

    @Override
    public List<Object> queryDecimalData(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute) {
        // [1] Sign
        // [2] Digit length of pre decimal point part
        // [3] Digit length of whole decimal including decimal point and minus
        String selectPart = "SIGN(" + attribute.getName() + "), LEN(ABS(ROUND(" + attribute.getName() + ", 0, 1)))," +
                " LEN(" + attribute.getName() + ")";
        String sql = "SELECT " + selectPart + " FROM " + dbName + "." + entity.getSchema() + "." + entity.getName() + ";";
        return jdbcDao.queryDecimalData(sql);
    }

    @Override
    public List<Object> querySign(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute) {
        String sql = "SELECT SIGN(" + attribute.getName() + ") FROM " + dbName + "." + entity.getSchema() + "." + entity.getName() + ";";
        return jdbcDao.querySign(sql);
    }

    @Override
    public void dropUniqueConstraint(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute) {
        String sql = "ALTER TABLE " + entity.getSchema() + "." + entity.getName() +
                " DROP CONSTRAINT " + attribute.getUniqueConstraintName() + ";";
        jdbcDao.executeSimpleSQLCommand(sql);
    }

    @Override
    public void createUniqueConstraint(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute) {
        String sql = "ALTER TABLE " + entity.getSchema() + "." + entity.getName() + " ADD CONSTRAINT " +
                attribute.getUniqueConstraintName() + " UNIQUE (" + attribute.getName() + ");";
        jdbcDao.executeSimpleSQLCommand(sql);
    }
}