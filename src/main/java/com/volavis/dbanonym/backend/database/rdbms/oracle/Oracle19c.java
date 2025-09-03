package com.volavis.dbanonym.backend.database.rdbms.oracle;

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
 * Oracle 19c.
 * <P>Info: database == catalog ; schema == user.
 * <P>To identify an object Oracle uses: "schema.object".
 */
public class Oracle19c extends DatabaseVersion implements OracleAdditionalMetadata {

    @Override
    public VersionID getVersionID() {
        return VersionID.ORACLE_19C;
    }

    @Override
    public Attribute getAttribute(JdbcDao dao, String dbName, Entity entity, Attribute attribute) {
        return OracleMapping.getAttribute(dao, this, dbName, entity, attribute);
    }

    @Override
    public String getEmptyResultSetQuery(String dbName, Entity entity) {
        return "SELECT * FROM " + entity.getSchema() + "." + entity.getName() + " WHERE 1=0";
    }

    @Override
    public String queryDatabaseName(JdbcDao jdbcDao) {
        String sql = "SELECT SYS_CONTEXT('USERENV','INSTANCE_NAME') FROM DUAL";
        return jdbcDao.queryDatabaseName(sql);
    }

    @Override
    public List<String> queryEntityNames(JdbcDao jdbcDao) {
        String sql = "SELECT (SELECT USER FROM DUAL), TABLE_NAME FROM USER_TABLES";
        return jdbcDao.queryEntityNamesWithSchema(sql);
    }

    @Override
    public Long queryRecordCount(JdbcDao jdbcDao, String dbName, Entity entity) {
        String sql = "SELECT COUNT(*) FROM " + entity.getSchema() + "." + entity.getName();
        return jdbcDao.queryRecordCount(sql);
    }

    @Override
    public List<DbDataType> queryDatabaseDataTypes(JdbcDao jdbcDao, String dbName, Entity entity) {
        return jdbcDao.queryDatabaseDataTypeRSMD(getEmptyResultSetQuery(dbName, entity));
    }

    @Override
    public List<String> queryCheckConstraints(JdbcDao jdbcDao, String dbName, Entity entity) {
        String sql = "SELECT SEARCH_CONDITION FROM USER_CONSTRAINTS WHERE TABLE_NAME = '" + entity.getName()
                + "' AND CONSTRAINT_TYPE = 'C' AND SEARCH_CONDITION_VC NOT LIKE '\"%\" IS NOT NULL'";
        return jdbcDao.queryCheckConstraints(sql);
    }

    @Override
    public List<UniqueConstraint> queryUniqueConstraints(JdbcDao jdbcDao, String dbName, Entity entity) {
        String sql = "SELECT B.COLUMN_NAME, B.CONSTRAINT_NAME FROM USER_CONSTRAINTS A" +
                " JOIN USER_CONS_COLUMNS B ON A.CONSTRAINT_NAME = B.CONSTRAINT_NAME" +
                " WHERE A.CONSTRAINT_TYPE = 'U' AND A.TABLE_NAME = '" + entity.getName() + "'";
        return jdbcDao.queryUniqueConstraints(sql);
    }

    @Override
    public void updateAllRowsWithNull(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute) {
        String namedParameter = "value";
        String sql = "UPDATE " + entity.getSchema() + "." + entity.getName() +
                " SET " + attribute.getName() + " = :" + namedParameter;
        jdbcDao.updateAllRowsWithSameValue(sql, namedParameter, null, Types.NULL);
    }

    @Override
    public void updateAllRowsWithDefaultValue(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute) {
        String sql = "UPDATE " + entity.getSchema() + "." + entity.getName() +
                " SET " + attribute.getName() + " = DEFAULT";
        jdbcDao.updateAllRowsWithSameValue(sql);
    }

    @Override
    public void updateAllRowsWithSameValue(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute, Object value) {
        String namedParameter = "value";
        String sql = "UPDATE " + entity.getSchema() + "." + entity.getName()
                + " SET " + attribute.getName() + " = :" + namedParameter;

        jdbcDao.updateAllRowsWithSameValue(sql, namedParameter, value, attribute.getJdbcDataType());
    }

    @Override
    public void batchUpdateAllRowsDifferentValues(JdbcDao jdbcDao, String dbName, int batchSize, Entity entity, Attribute valueAttr, List<Attribute> pkList, List<ValueWithPKs> valueList) {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(entity.getSchema()).append(".").append(entity.getName()).append(" SET ").append(valueAttr.getName()).append(" = ? WHERE ");
        for(int i = 0; i < pkList.size(); i++) {
            sql.append(pkList.get(i).getName()).append(" = ?");
            if(i < pkList.size() - 1) {
                sql.append(" AND ");
            }
        }

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
        sql.append(" FROM ").append(entity.getSchema()).append(".").append(entity.getName());
        jdbcDao.queryEntityPKs(sql.toString(), aliasPrefix, pkList.size(), valueList);
    }

    @Override
    public List<Object> queryLengths(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute) {
        String selectPart;
        switch (attribute.getGuiID()) {
            case CHARACTERS:
            case ORACLE_CHARACTERS:
                selectPart = "LENGTH(TRIM(" + attribute.getName() + "))";
                break;
            case BINARY:
                selectPart = "LENGTH(" + attribute.getName() + ")";
                break;
            case INTEGER:
                selectPart = "LENGTH(ABS(" + attribute.getName() + "))";
                break;
            default:
                return null;
        }
        String sql = "SELECT " + selectPart + " FROM " +  entity.getSchema() + "." + entity.getName();
        return jdbcDao.queryLengths(sql);
    }

    @Override
    public List<Object> queryDecimalData(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute) {
        // [1] Sign
        // [2] Digit length of pre decimal point part
        // [3] Digit length of whole decimal including decimal point and minus (abs() does not "work" with float/double!)
        String selectPart = "SIGN(" + attribute.getName() + "), LENGTH(ABS(TRUNC(TO_NUMBER(" + attribute.getName() + "))))," +
                " LENGTH(TO_NUMBER(" + attribute.getName() + "))";
        String sql = "SELECT " + selectPart + " FROM " + entity.getSchema() + "." + entity.getName();
        return jdbcDao.queryDecimalData(sql);
    }

    @Override
    public List<Object> querySign(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute) {
        String sql = "SELECT SIGN(" + attribute.getName() + ") FROM " + entity.getSchema() + "." + entity.getName();
        return jdbcDao.querySign(sql);
    }

    @Override
    public String queryQualifierCharacterDataTypes(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute) {
        String sql = "SELECT CHAR_USED FROM USER_TAB_COLUMNS WHERE TABLE_NAME = '" + entity.getName() +
                "' AND COLUMN_NAME = '" + attribute.getName() + "'";
        return jdbcDao.queryQualifierCharacterDataTypes(sql);
    }

    @Override
    public void dropUniqueConstraint(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute) {
        String sql = "ALTER TABLE " + entity.getSchema() + "." + entity.getName() +
                " DROP CONSTRAINT " + attribute.getUniqueConstraintName();
        jdbcDao.executeSimpleSQLCommand(sql);
    }

    @Override
    public void createUniqueConstraint(JdbcDao jdbcDao, String dbName, Entity entity, Attribute attribute) {
        String sql = "ALTER TABLE " + entity.getSchema() + "." + entity.getName() + " ADD CONSTRAINT " +
                attribute.getUniqueConstraintName() + " UNIQUE (" + attribute.getName() + ")";
        jdbcDao.executeSimpleSQLCommand(sql);
    }
}