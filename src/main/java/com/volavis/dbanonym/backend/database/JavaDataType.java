package com.volavis.dbanonym.backend.database;

import com.volavis.dbanonym.backend.database.attributes.rdbmsunspecific.*;

/**
 * Enum for the supported java data types (and custom data types).
 */
public enum JavaDataType {

    STRING("String"),
    CLOB("Clob"),
    BYTE("Byte"),
    SHORT("Short"),
    INTEGER("Integer"),
    LONG("Long"),
    BIGINTEGER("BigInteger"),
    FLOAT("Float"),
    DOUBLE("Double"),
    BIGDECIMAL("BigDecimal"),
    BOOLEAN("Boolean"),
    BYTEARRAY("Byte[]"),
    BLOB("Blob"),
    DATE("Date"),
    TIME("Time"),
    TIMESTAMP("Timestamp"),
    UNKNOWN("Unknown"),

    //=================================== Custom data types =========================================//

    MYSQL_ENUM("Enum"),
    ORACLE_STRING("String"),
    ORACLE_BIGDECIMAL("BigDecimal");

    private final String name;

    /**
     * Constructor.
     * @param name Readable name of the data type.
     */
    JavaDataType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Maps a java class name to a Attribute subclass.
     * Only used when the attribute mapping returns UnknownAttribute and the driver's Java data type will be used.
     * @param attribute Attribute whose properties will be copied.
     * @return Specific attribute.
     */
    public static Attribute getAttributeFromClassName(Attribute attribute) {
        switch (attribute.getJavaClassNameDriver()) {
            case "java.lang.String":
            case "java.sql.Clob":                               // TODO: CLOB
                return new StringAttribute(attribute);
            case "java.lang.Byte":
                return new ByteAttribute(attribute);
            case "java.lang.Short":
                return new ShortAttribute(attribute);
            case "java.lang.Integer":
                return new IntegerAttribute(attribute);
            case "java.lang.Long":
                return new LongAttribute(attribute);
            case "java.math.BigInteger":
                return new BigIntegerAttribute(attribute);
            case "java.lang.Float":
                return new FloatAttribute(attribute);
            case "java.lang.Double":
                return new DoubleAttribute(attribute);
            case "java.math.BigDecimal":
                return new BigDecimalAttribute(attribute);
            case "java.lang.Boolean":
                return new BooleanAttribute(attribute);
            case "[B":
            case "java.sql.Blob":                               // TODO: BLOB
                return new ByteArrayAttribute(attribute);
            case "java.sql.Date":
                return new DateAttribute(attribute);
            case "java.sql.Time":
                return new TimeAttribute(attribute);
            case "java.sql.Timestamp":
                return new TimestampAttribute(attribute);
            default:
                return new UnknownAttribute(attribute);
        }
    }
}