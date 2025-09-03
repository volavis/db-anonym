package com.volavis.dbanonym.backend.database.rdbms.mysql;

import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.backend.database.attributes.rdbmsspecific.MysqlEnumAttribute;
import com.volavis.dbanonym.backend.database.attributes.rdbmsunspecific.*;
import com.volavis.dbanonym.backend.database.rdbms.DatabaseVersion;
import com.volavis.dbanonym.backend.database.jdbc.JdbcDao;

import java.sql.Types;

/**
 * This class contains the getAttribute method that maps an UnknownAttribute to a specific attribute class.
 * <P>Additionally the attributes can be changed, like e.g. the JDBC data type, precision, scale, etc.
 */
public final class MysqlMapping {

    /**
     * Constructor.
     */
    private MysqlMapping() {
        throw new UnsupportedOperationException();
    }

    /**
     * Maps an UnknownAttribute to a specific attribute class.
     * <P>Additionally the attribute can be changed, like e.g. the JDBC data type, precision, scale, etc.
     * @param dao JDBC DAO.
     * @param dbVersion The database version.
     * @param dbName Name of the database.
     * @param entity The entity the attribute belongs to.
     * @param attribute Attribute which is currently being considered.
     * @return Specific attribute.
     */
    public static Attribute getAttribute(JdbcDao dao, DatabaseVersion dbVersion, String dbName, Entity entity, Attribute attribute) {

        switch (attribute.getDatabaseDataType()) {
            case "tinyint":
                return getAttributeTinyint(attribute);
            case "smallint":
                return getAttributeSmallint(attribute);
            case "mediumint":
                return getAttributeMediumint(attribute);
            case "int":
                return getAttributeInt(attribute);
            case "bigint":
                return getAttributeBigint(attribute);
            case "decimal":
                return new BigDecimalAttribute(attribute);
            case "float":
                return new FloatAttribute(attribute);
            case "double":
                return new DoubleAttribute(attribute);
            case "bit":
                return getAttributeBit(attribute);
            case "char":
            case "varchar":
            case "tinytext":
            case "text":
            case "mediumtext":
                return new StringAttribute(attribute);
            case "binary":
            case "varbinary":
            case "tinyblob":
            case "blob":
            case "mediumblob":
                return new ByteArrayAttribute(attribute);
            case "date":
                return new DateAttribute(attribute);
            case "time":
                return new TimeAttribute(attribute);
            case "datetime":
            case "timestamp":
                return new TimestampAttribute(attribute);
            case "enum":
                return new MysqlEnumAttribute(dao, dbVersion, dbName, entity, attribute);
            default:
                return attribute;
        }
    }

    /**
     * Case TINYINT.
     * @param attribute Attribute which is currently being considered.
     * @return Specific attribute.
     */
    private static Attribute getAttributeTinyint(Attribute attribute) {
        if(attribute.getJavaClassNameDriver().equals("java.lang.Boolean")) {
            return new BooleanAttribute(attribute);
        } else {
            if(!attribute.isSigned()) {
                attribute.setJdbcDataType(Types.VARCHAR);
            }
            return new IntegerAttribute(attribute);
        }
    }

    /**
     * Case SMALLINT.
     * @param attribute Attribute which is currently being considered.
     * @return Specific attribute.
     */
    private static Attribute getAttributeSmallint(Attribute attribute) {
        if(!attribute.isSigned()) {
            attribute.setJdbcDataType(Types.VARCHAR);
        }
        return new IntegerAttribute(attribute);
    }

    /**
     * Case MEDIUMINT.
     * @param attribute Attribute which is currently being considered.
     * @return Specific attribute.
     */
    private static Attribute getAttributeMediumint(Attribute attribute) {
        if(!attribute.isSigned()) {
            attribute.setJdbcDataType(Types.VARCHAR);
        }
        return attribute;   // TODO: Different value range for MEDIUMINT
    }

    /**
     * Case INT.
     * @param attribute Attribute which is currently being considered.
     * @return Specific attribute.
     */
    private static Attribute getAttributeInt(Attribute attribute) {
        if(attribute.isSigned()) {
            return new IntegerAttribute(attribute);
        } else {
            attribute.setJdbcDataType(Types.VARCHAR);
            return new LongAttribute(attribute);
        }
    }

    /**
     * Case BIGINT.
     * @param attribute Attribute which is currently being considered.
     * @return Specific attribute.
     */
    private static Attribute getAttributeBigint(Attribute attribute) {
        if(attribute.isSigned()) {
            return new LongAttribute(attribute);
        } else {
            attribute.setJdbcDataType(Types.VARCHAR);
            return new BigIntegerAttribute(attribute);
        }
    }

    /**
     * Case BIT.
     * @param attribute Attribute which is currently being considered.
     * @return Specific attribute.
     */
    private static Attribute getAttributeBit(Attribute attribute) {
        if(attribute.getPrecision() == 1) {
            return new BooleanAttribute(attribute);
        } else {
            return attribute;    // TODO: BIT(>1)
        }
    }
}