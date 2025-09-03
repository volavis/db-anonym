package com.volavis.dbanonym.backend.database.rdbms.mssql;

import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.backend.database.attributes.rdbmsunspecific.*;
import com.volavis.dbanonym.backend.database.jdbc.JdbcDao;
import com.volavis.dbanonym.backend.database.rdbms.DatabaseVersion;

/**
 * This class contains the getAttribute method that maps an UnknownAttribute to a specific attribute class.
 * <P>Additionally the attributes can be changed, like e.g. the JDBC data type, precision, scale, etc.
 */
public final class MssqlMapping {

    /**
     * Constructor.
     */
    private MssqlMapping() {
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
            case "char":
            case "varchar":
            case "text":
            case "nchar":
            case "nvarchar":
            case "ntext":
                return new StringAttribute(attribute);
            case "bit":
                return new BooleanAttribute(attribute);
            case "tinyint":
            case "smallint":
                return new ShortAttribute(attribute);
            case "int":
                return new IntegerAttribute(attribute);
            case "bigint":
                return new LongAttribute(attribute);
            case "decimal":
            case "numeric":
            case "smallmoney":                  // TODO: Money and Smallmoney have a value range!
            case "money":
                return new BigDecimalAttribute(attribute);
            case "float":
                return new DoubleAttribute(attribute);
            case "real":
                return new FloatAttribute(attribute);
            case "binary":
            case "varbinary":
            case "image":
                return new ByteArrayAttribute(attribute);
            case "datetime":
            case "datetime2":
            case "smalldatetime":
                return new TimestampAttribute(attribute);
            case "date":
                return new DateAttribute(attribute);
            case "time":
                return new TimeAttribute(attribute);
            default:
                return attribute;
        }
    }
}