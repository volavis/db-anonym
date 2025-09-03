package com.volavis.dbanonym.backend.database.rdbms.oracle;

import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.backend.database.attributes.rdbmsspecific.OracleBigDecimalAttribute;
import com.volavis.dbanonym.backend.database.attributes.rdbmsspecific.OracleStringAttribute;
import com.volavis.dbanonym.backend.database.attributes.rdbmsunspecific.*;
import com.volavis.dbanonym.backend.database.jdbc.JdbcDao;
import com.volavis.dbanonym.backend.database.jdbc.types.JdbcDecimalType;
import com.volavis.dbanonym.backend.database.rdbms.DatabaseVersion;

import java.sql.Types;

/**
 * This class contains the getAttribute method that maps an UnknownAttribute to a specific attribute class.
 * <P>Additionally the attributes can be changed, like e.g. the JDBC data type, precision, scale, etc.
 */
public final class OracleMapping {

    /**
     * Constructor.
     */
    private OracleMapping() {
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
            case "CHAR":
            case "NCHAR":
            case "VARCHAR2":
            case "NVARCHAR2":
                return new OracleStringAttribute(dao, dbVersion, dbName, entity, attribute);
            case "NUMBER":
                return getAttributeNumber(attribute);
            case "BINARY_FLOAT":
                return getAttributeBinaryFloat(attribute);
            case "BINARY_DOUBLE":
                return getAttributeBinaryDouble(attribute);
            case "DATE":
            case "TIMESTAMP":
            case "TIMESTAMP WITH LOCAL TIME ZONE":
            case "TIMESTAMP WITH TIME ZONE":
                return new TimestampAttribute(attribute);
            default:
                return attribute;
        }
    }

    /**
     * Case NUMBER.
     * @param attribute Attribute which is currently being considered.
     * @return Specific attribute.
     */
    private static Attribute getAttributeNumber(Attribute attribute) {
        if(attribute.getPrecision() == 0) {
            // Default precision is 38
            attribute.setPrecision(38);
        } else if(attribute.getPrecision() == 63) {
            // REAL, binary precision of 63
            attribute.setPrecision(18);
        } else if(attribute.getPrecision() == 126) {
            // FLOAT, binary precision of 126
            attribute.setPrecision(38);
        }

        if(attribute.getScale() < -84) {
            // No Precision specified = values are stored "as given"
            attribute.setScale(JdbcDecimalType.STORE_VALUES_AS_GIVEN_SCALE);
        }
        return new OracleBigDecimalAttribute(attribute);
    }

    /**
     * Case BINARY_FLOAT.
     * @param attribute Attribute which is currently being considered.
     * @return Specific attribute.
     */
    private static Attribute getAttributeBinaryFloat(Attribute attribute) {
        attribute.setJdbcDataTypeValidation(Types.REAL);
        attribute.setJdbcDataType(Types.FLOAT);
        return new FloatAttribute(attribute);
        // Problem metadata extraction: Has to use TO_NUMBER()! Adds additional ~1-2 decimal places!
    }

    /**
     * Case BINARY_DOUBLE.
     * @param attribute Attribute which is currently being considered.
     * @return Specific attribute.
     */
    private static Attribute getAttributeBinaryDouble(Attribute attribute) {
        attribute.setJdbcDataTypeValidation(Types.DOUBLE);
        attribute.setJdbcDataType(Types.DOUBLE);
        return new DoubleAttribute(attribute);
        // Problem metadata extraction: Has to use TO_NUMBER()! Adds additional ~1-2 decimal places!
    }
}