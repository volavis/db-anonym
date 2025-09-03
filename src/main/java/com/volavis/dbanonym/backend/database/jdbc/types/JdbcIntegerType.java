package com.volavis.dbanonym.backend.database.jdbc.types;

import java.math.BigInteger;
import java.sql.Types;

/**
 * Enum that represents the jdbc integer types with their bit lengths.
 * <P>It contains methods for working with jdbc integer types.
 */
public enum JdbcIntegerType {

    TINYINT(Types.TINYINT, 8),
    SMALLINT(Types.SMALLINT, 16),
    INTEGER(Types.INTEGER, 32),
    BIGINT(Types.BIGINT, 64);

    private final int type;
    private final int bits;

    /**
     * Constructor.
     * @param type Jdbc decimal type.
     * @param bits Bit length.
     */
    JdbcIntegerType(int type, int bits) {
        this.type = type;
        this.bits = bits;
    }

    /**
     * Retrieves an enum object depending on the jdbc type.
     * @param type Jdbc type.
     * @return JdbcIntegerType object.
     */
    public static JdbcIntegerType getType(int type) {
        for(JdbcIntegerType integerType : JdbcIntegerType.values()) {
            if(integerType.getType() == type) {
                return integerType;
            }
        }
        return null;
    }

    /**
     * Retrieves the min or max value for the jdbc type.
     * @param getMin Get min or max value?
     * @param jdbcType Jdbc type.
     * @param signed Is the attribute signed?
     * @return Min or max BigInteger value.
     */
    public static BigInteger getMinOrMax(boolean getMin, int jdbcType, boolean signed) {
        JdbcIntegerType type = getType(jdbcType);
        if(type != null) {
            return getMin ? getMinValue(type, signed) : getMaxValue(type, signed);
        }
        return null;
    }

    /**
     * Calculates the minimal value for the JdbcIntegerType considering its bit length and if it is signed.
     * @param type Jdbc integer type enum.
     * @param signed Is the attribute signed?
     * @return Minimal BigInteger value.
     */
    private static BigInteger getMinValue(JdbcIntegerType type, boolean signed) {
        if(signed) {
            return new BigInteger("-2").pow(type.getBits() - 1);
        } else {
            return new BigInteger("0");
        }
    }

    /**
     * Calculates the maximal value for the JdbcIntegerType considering its bit length and if it is signed.
     * @param type Jdbc integer type enum.
     * @param signed Is the attribute signed?
     * @return Maximal BigInteger value.
     */
    private static BigInteger getMaxValue(JdbcIntegerType type, boolean signed) {
        if (signed) {
            return new BigInteger("2").pow(type.getBits() - 1).subtract(new BigInteger("1"));
        } else {
            return new BigInteger("2").pow(type.getBits()).subtract(new BigInteger("1"));
        }
    }

    public int getType() {
        return type;
    }

    public int getBits() {
        return bits;
    }
}