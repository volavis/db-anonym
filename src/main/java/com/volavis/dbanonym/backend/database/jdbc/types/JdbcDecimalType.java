package com.volavis.dbanonym.backend.database.jdbc.types;

import com.volavis.dbanonym.backend.database.Attribute;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.Types;

/**
 * Enum that represents the jdbc decimal types with their mantissa lengths.
 * <P>It contains methods for working with jdbc decimal types.
 */
public enum JdbcDecimalType {

    REAL(Types.REAL, 7),
    FLOAT(Types.FLOAT, 15),
    DOUBLE(Types.DOUBLE, 15),
    DECIMAL(Types.DECIMAL, null),
    NUMERIC(Types.NUMERIC, null);

    private final int type;
    private final Integer mantissa;     // = max decimal places

    /**
     * Used when the scale is not specified and any decimal value can be stored.
     * Oracle: Must be smaller than -84.
     */
    public static int STORE_VALUES_AS_GIVEN_SCALE = -111;

    /**
     * Constructor.
     * @param type Jdbc decimal type.
     * @param mantissa Mantissa length.
     */
    JdbcDecimalType(int type, Integer mantissa) {
        this.type = type;
        this.mantissa = mantissa;
    }

    /**
     * Retrieves an enum object depending on the jdbc type.
     * @param type Jdbc type.
     * @return JdbcDecimalType object.
     */
    public static JdbcDecimalType getType(int type) {
        for(JdbcDecimalType decimalType : JdbcDecimalType.values()) {
            if(decimalType.getType() == type) {
                return decimalType;
            }
        }
        return null;
    }

    /**
     * Retrieves the mantissa length depending on the jdbc type.
     * @param jdbcType Jdbc type.
     * @return Mantissa length or null.
     */
    public static Integer getDecimalPlaces(int jdbcType) {
        JdbcDecimalType type = getType(jdbcType);
        if(type != null) {
            return type.getMantissa();
        }
        return null;
    }

    /**
     * Generates the min or max value depending on the pre and post decimal places.
     * Used only with DECIMAL database types!
     * @param getMin Get min or max value?
     * @param attribute Attribute that holds the database data type information.
     * @return Min or max BigDecimal value.
     */
    public static BigDecimal generateMinOrMax(boolean getMin, Attribute attribute) {
        int precision = attribute.getPrecision();   // All digits
        int scale = attribute.getScale();           // Post decimal point length
        int preDecimalPlaces = precision - scale;   // Pre decimal point length

        StringBuilder resultStr = new StringBuilder();
        if(getMin) {
            resultStr.append("-");
        }
        resultStr.append(StringUtils.repeat("9", preDecimalPlaces));
        if(scale > 0) {
            resultStr.append(".");
            resultStr.append(StringUtils.repeat("9", scale));
        }

        return new BigDecimal(resultStr.toString());
    }

    /**
     * Checks if the BigDecimal value's decimal places are <= the parameter decimalPlaces.
     * @param decimalPlaces Max decimal places.
     * @param bigDecimal Value that is being examined.
     * @return valid = true, invalid = false.
     */
    public static boolean checkPostDecimalPlaces(int decimalPlaces, BigDecimal bigDecimal) {
        String bigDecimalString = bigDecimal.abs().toPlainString();
        String[] parts = bigDecimalString.split("\\.");
        if(parts.length == 2) {
            // Decimal point found
            int postPartLength = parts[1].length();
            return postPartLength <= decimalPlaces;
        } else {
            return true;
        }
    }

    /**
     * Checks the pre and post decimal places of the BigDecimal value. Used only with DECIMAL database types!
     * @param attribute Attribute that holds the database data type information.
     * @param bigDecimal Value that is being examined.
     * @return valid = true, invalid = false.
     */
    public static boolean checkPreAndPostDecimalPlaces(Attribute attribute, BigDecimal bigDecimal) {
        int precision = attribute.getPrecision();
        int scale = attribute.getScale();

        String bigDecimalString = bigDecimal.abs().toPlainString();
        String[] parts = bigDecimalString.split("\\.");

        return verifyAttributePrecisionGreaterScale(precision, scale, parts);
    }

    /**
     * Verification-logic for when the precision is greater than scale.
     * @param precision The attribute's precision.
     * @param scale The attribute's scale.
     * @param parts Different parts of the BigDecimal value (split at the decimal point).
     * @return valid = true, invalid = false.
     */
    private static boolean verifyAttributePrecisionGreaterScale(int precision, int scale, String[] parts) {
        int preDecimalPlaces = precision - scale;
        if (parts.length == 1) {
            // No decimal point found
            return parts[0].length() <= preDecimalPlaces;
        } else if(parts.length == 2) {
            // Decimal point found
            int prePartLength = parts[0].length();
            int postPartLength = parts[1].length();
            return prePartLength <= preDecimalPlaces && postPartLength <= scale;
        }
        return false;
    }

    /**
     * Used only with the RDBMS Oracle.
     * Problem: In Oracle the scale can be greater than the precision! + The scale can be unspecified!
     * Checks the pre and post decimal places of the BigDecimal value.
     * @param attribute Attribute that holds the database data type information.
     * @param bigDecimal Value that is being examined.
     * @return valid = true, invalid = false.
     */
    public static boolean oracleCheckPreAndPostDecimalPlaces(Attribute attribute, BigDecimal bigDecimal) {
        int precision = attribute.getPrecision();
        int scale = attribute.getScale();

        String bigDecimalString = bigDecimal.abs().toPlainString();
        String[] parts = bigDecimalString.split("\\.");

        if(scale >= precision) {
            return verifyAttributeScaleGreaterPrecision(precision, scale, parts);
        } else if(scale >= 0 && scale < 38) {
            return verifyAttributePrecisionGreaterScale(precision, scale, parts);
        } else if(scale == STORE_VALUES_AS_GIVEN_SCALE) {
            // If the scale is not specified any number can be stored depending on the precision.
            return true;
        } else {
            // Unconsidered case.
            return true;
        }
    }

    /**
     * Verification-logic for when the scale is greater than the precision.
     * <P>There must only be a 0 before the decimal point!
     * <P>Number of decimal places = Scale!
     * <P>Scale - precision = Minimum number of zeros after the decimal point!
     * @param precision The attribute's precision.
     * @param scale The attribute's scale.
     * @param parts Different parts of the BigDecimal value (split at the decimal point).
     * @return valid = true, invalid = false.
     */
    private static boolean verifyAttributeScaleGreaterPrecision(int precision, int scale, String[] parts) {
        if (parts.length == 1) {
            // No decimal point found
            return false;
        } else if(parts.length == 2) {
            // Decimal point found
            int postPartLength = parts[1].length();
            int zeroPartLength = scale - precision;
            return parts[0].equals("0") && postPartLength <= scale && checkZeros(parts[1], zeroPartLength);
        }
        return false;
    }

    /**
     * Checks the minimal number of zeros after the decimal point.
     * @param part Post decimal point String.
     * @return valid = true, invalid = false.
     */
    private static boolean checkZeros(String part, int zeroPartLength) {
        if(part.length() >= zeroPartLength) {
            return Integer.parseInt(part.substring(0, zeroPartLength)) == 0;
        }
        return false;
    }

    public int getType() {
        return type;
    }

    public Integer getMantissa() {
        return mantissa;
    }
}