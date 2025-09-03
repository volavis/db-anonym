package com.volavis.dbanonym.backend.database.attributes.rdbmsunspecific;

import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.anonymization.options.GuiID;
import com.volavis.dbanonym.backend.database.JavaDataType;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Attribute whose Java data type is unknown.
 */
public class UnknownAttribute extends Attribute {

    /**
     * Constructor.
     */
    public UnknownAttribute() {
        super(JavaDataType.UNKNOWN, GuiID.UNKNOWN);
    }

    /**
     * Constructor.
     * @param attribute Attribute whose properties will be copied.
     */
    public UnknownAttribute(Attribute attribute) {
        super(attribute, JavaDataType.UNKNOWN, GuiID.UNKNOWN);
    }

    /**
     * Constructor.
     * @param resultSetMetaData Holds information about the types/ properties of the columns in a ResultSet object.
     * @param column Index of current column.
     */
    public UnknownAttribute(ResultSetMetaData resultSetMetaData, int column) throws SQLException {
        this(null);

        setName(resultSetMetaData.getColumnName(column));
        setJavaClassNameDriver(resultSetMetaData.getColumnClassName(column));
        setPrecision(resultSetMetaData.getPrecision(column));
        setScale(resultSetMetaData.getScale(column));
        setSigned(resultSetMetaData.isSigned(column));

        int jdbcType = resultSetMetaData.getColumnType(column);
        setJdbcDataTypeDriver(jdbcType);
        setJdbcDataTypeValidation(jdbcType);
        setJdbcDataType(jdbcType);

        int nullableInt = resultSetMetaData.isNullable(column);
        setNullable(nullableInt != ResultSetMetaData.columnNoNulls);
    }
}