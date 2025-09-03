package com.volavis.dbanonym.backend.anonymization.options.rndoption;

import com.volavis.dbanonym.backend.database.jdbc.types.JdbcIntegerType;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Contains several methods that generate random values depending on the Java data type.
 */
public final class RndValueGenerator {

    /**
     * Constructor.
     */
    private RndValueGenerator() {
        throw new UnsupportedOperationException();
    }

    /**
     * Generates a random String with a fixed character length.
     * @param length Character length.
     * @param letters Use letters?
     * @param numbers Use numbers?
     * @return Random String.
     */
    public static String generateStringFixedLength(Integer length, boolean letters, boolean numbers) {
        if(length != null) {
            if(length <= 0) {
                throw new IllegalArgumentException("Length <= 0!");
            }
            return RandomStringUtils.random(length, letters, numbers);  // 62 possible chars
        } else {
            return null;    // If the database value is null (length == null), the value stays null!
        }
    }

    /**
     * Generates a random String that has a character length between min and max bounds.
     * @param minValue Minimal character length.
     * @param maxValue Maximal character length.
     * @param letters Use letters?
     * @param numbers Use numbers?
     * @return Random String.
     */
    public static String generateStringInterval(int minValue, int maxValue, boolean letters, boolean numbers) {
        if(maxValue < minValue) {
            throw new IllegalArgumentException("Max < Min!");
        }

        int length;
        if(maxValue != Integer.MAX_VALUE) {
            length = ThreadLocalRandom.current().nextInt(minValue, maxValue + 1);
        } else {
            length = ThreadLocalRandom.current().nextInt(minValue, maxValue);
        }
        return generateStringFixedLength(length, letters, numbers);
    }

    /**
     * Generates a random String with a fixed character length.
     * Unique: Only uses lower case letters = 36 possible chars
     * @param length Character length.
     * @param letters Use letters?
     * @param numbers Use numbers?
     * @return Random String.
     */
    public static String uniqueGenerateStringFixedLength(Integer length, boolean letters, boolean numbers) {
        if(length != null) {
            if(length <= 0) {
                throw new IllegalArgumentException("Length <= 0!");
            }
            return RandomStringUtils.random(length, letters, numbers).toLowerCase();
        } else {
            return null;    // If the database value is null (length == null), the value stays null!
        }
    }

    /**
     * Generates a random String that has a character length between min and max bounds.
     * Unique: Only uses lower case letters = 36 possible chars
     * @param minValue Minimal character length.
     * @param maxValue Maximal character length.
     * @param letters Use letters?
     * @param numbers Use numbers?
     * @return Random String.
     */
    public static String uniqueGenerateStringInterval(int minValue, int maxValue, boolean letters, boolean numbers) {
        if(maxValue < minValue) {
            throw new IllegalArgumentException("Max < Min!");
        }

        int length;
        if(maxValue != Integer.MAX_VALUE) {
            length = ThreadLocalRandom.current().nextInt(minValue, maxValue + 1);
        } else {
            length = ThreadLocalRandom.current().nextInt(minValue, maxValue);
        }
        return uniqueGenerateStringFixedLength(length, letters, numbers);
    }

    /**
     * Generates a random BigInteger with a fixed digit length between the Jdbc value limits.
     * @param type Jdbc integer type.
     * @param typeSigned Is the integer attribute signed?
     * @param length Digit length.
     * @param sign Plus (true), minus (false), null of the database value not yet anonymized.
     * @param useSign Does the user want to keep the minus or plus?
     * @return Random BigInteger.
     */
    public static BigInteger generateBigIntegerFixedLength(int type, boolean typeSigned, Integer length, Boolean sign, boolean useSign) {
        if(length != null) {
            if(length <= 0) {
                throw new IllegalArgumentException("Length <= 0!");
            }

            BigInteger minValue = JdbcIntegerType.getMinOrMax(true, type, typeSigned);
            BigInteger maxValue = JdbcIntegerType.getMinOrMax(false, type, typeSigned);
            BigInteger tmpBigInteger;

            do {
                tmpBigInteger = generateBigInteger(length, sign, useSign);
                if(tmpBigInteger == null) {
                    return null;
                }
            } while(tmpBigInteger.compareTo(minValue) < 0 || tmpBigInteger.compareTo(maxValue) > 0);

            return tmpBigInteger;

        } else {
            return null;
        }
    }

    /**
     * Generates a BigInteger depending on the length
     * @param length Digit length.
     * @param sign Plus (true), minus (false), null of the database value not yet anonymized.
     * @param useSign Does the user want to keep the minus or plus?
     * @return Generated BigInteger with a fixed digit length.
     */
    private static BigInteger generateBigInteger(Integer length, Boolean sign, boolean useSign) {
        String tmpString;
        if(length > 1) {
            do {
                tmpString = generateStringFixedLength(length, false, true);
            } while(tmpString.charAt(0) == '0');
        } else {
            tmpString = generateStringFixedLength(length, false, true);
        }

        if(useSign) {
            // Keep sign
            if(sign == null) {
                return null;
            }
            if(!sign) {
                tmpString = "-" + tmpString;
            }
        } else {
            // Random sign
            boolean isPositive = generateBoolean();
            if(!isPositive) {
                tmpString = "-" + tmpString;
            }
        }
        return new BigInteger(tmpString);
    }

    /**
     * Generates a random BigInteger between the Jdbc value limits.
     * @param type Jdbc integer type.
     * @param signed Is the integer attribute signed?
     * @return Random BigInteger.
     */
    public static BigInteger generateBigIntegerValueRange(int type, boolean signed) {
        BigInteger minValue = JdbcIntegerType.getMinOrMax(true, type, signed);
        BigInteger maxValue = JdbcIntegerType.getMinOrMax(false, type, signed);
        return generateBigIntegerInterval(minValue, maxValue);
    }

    /**
     * Generates a random BigInteger between min and max bounds.
     * @param minValue Minimal BigInteger value.
     * @param maxValue Maximal BigInteger value.
     * @return Random BigInteger.
     */
    public static BigInteger generateBigIntegerInterval(BigInteger minValue, BigInteger maxValue) {
        if(minValue == null || maxValue == null) {
            throw new IllegalArgumentException("Min/Max is null!");
        }
        if(maxValue.compareTo(minValue) <= 0) {
            throw new IllegalArgumentException("Max <= Min!");
        }

        BigInteger result;
        if(minValue.signum() >= 0) {
            result = bothValuesPositive(minValue, maxValue);
        } else if(maxValue.signum() <= 0) {
            result = bothValuesNegative(minValue, maxValue);
        } else {
            result = minNegativeMaxPositive(minValue, maxValue);
        }
        return result;
    }

    /**
     * Min and max values are both positive.
     * @param minValue Minimal BigInteger value.
     * @param maxValue Maximal BigInteger value.
     * @return Random BigInteger.
     */
    private static BigInteger bothValuesPositive(BigInteger minValue, BigInteger maxValue) {
        BigInteger result;
        do {
            result = new BigInteger(maxValue.bitLength(), ThreadLocalRandom.current());
        } while (result.compareTo(maxValue) > 0 || result.compareTo(minValue) < 0);
        return result;
    }

    /**
     * Min and max values are both negative.
     * @param minValue Minimal BigInteger value.
     * @param maxValue Maximal BigInteger value.
     * @return Random BigInteger.
     */
    private static BigInteger bothValuesNegative(BigInteger minValue, BigInteger maxValue) {
        BigInteger result;
        BigInteger tmpResult;
        BigInteger minNegated = minValue.negate();
        BigInteger maxNegated = maxValue.negate();
        do {
            tmpResult = new BigInteger(minNegated.bitLength(), ThreadLocalRandom.current());
        } while(tmpResult.compareTo(minNegated) > 0 || tmpResult.compareTo(maxNegated) < 0);
        result = tmpResult.negate();
        return result;
    }

    /**
     * The min value is negative and the max value is positive.
     * @param minValue Minimal BigInteger value.
     * @param maxValue Maximal BigInteger value.
     * @return Random BigInteger.
     */
    private static BigInteger minNegativeMaxPositive(BigInteger minValue, BigInteger maxValue) {
        BigInteger result;
        boolean isPositive = generateBoolean();
        if(isPositive) {
            result = positiveSide(maxValue);
        } else {
            result = negativeSide(minValue);
        }
        return result;
    }

    /**
     * Positive side.
     * @param maxValue Maximal BigInteger value.
     * @return Random BigInteger.
     */
    private static BigInteger positiveSide(BigInteger maxValue) {
        BigInteger result;
        do {
            result = new BigInteger(maxValue.bitLength(), ThreadLocalRandom.current());
        } while (result.compareTo(maxValue) > 0);
        return result;
    }

    /**
     * Negative side.
     * @param minValue Minimal BigInteger value.
     * @return Random BigInteger.
     */
    private static BigInteger negativeSide(BigInteger minValue) {
        BigInteger result;
        BigInteger tmpResult;
        BigInteger minNegated = minValue.negate();
        do {
            tmpResult = new BigInteger(minNegated.bitLength(), ThreadLocalRandom.current());
        } while(tmpResult.compareTo(minNegated) > 0);
        result = tmpResult.negate();
        return result;
    }

    /**
     * Generates a random BigDecimal with fixed pre and post decimal point lengths.
     * @param preLength Pre decimal point length.
     * @param postLength Post decimal point length.
     * @param sign Plus (true), minus (false), null of the database value not yet anonymized.
     * @param useSign Does the user want to keep the minus or plus?
     * @return Random BigDecimal.
     */
    public static BigDecimal generateBigDecimalFixedLength(Integer preLength, Integer postLength, Boolean sign, boolean useSign) {
        if(preLength != null && postLength != null && sign != null) {
            if(preLength <= 0) {
                throw new IllegalArgumentException("Pre decimal point length <= 0!");
            }

            String preStr = createPreDecimalPointString(preLength);
            String result = createCompleteString(postLength, preStr);
            result = addSign(sign, useSign, result);

            return new BigDecimal(result);

        } else {
            return null;
        }
    }

    /**
     * Creates a String for all digits before the decimal point.
     * @param preLength Pre decimal point length.
     * @return String for all digits before the decimal point.
     */
    private static String createPreDecimalPointString(Integer preLength) {
        String preStr;
        if (preLength > 1) {
            do {
                preStr = generateStringFixedLength(preLength, false, true);
            } while (preStr.charAt(0) == '0');
        } else {
            preStr = generateStringFixedLength(preLength, false, true);
        }
        return preStr;
    }

    /**
     * Creates a String for the whole decimal value (pre + post part).
     * @param postLength Post decimal point length.
     * @param preStr String for all digits before the decimal point.
     * @return String for the whole decimal value.
     */
    private static String createCompleteString(Integer postLength, String preStr) {
        String result;
        String postStr;
        if(postLength > 1) {
            do {
                postStr = generateStringFixedLength(postLength, false, true);
            } while(postStr.charAt(postStr.length() - 1) == '0');
            result = preStr + "." + postStr;
        } else if(postLength == 1) {
            postStr = generateStringFixedLength(postLength, false, true);
            result = preStr + "." + postStr;
        } else {
            // Has no decimal places
            result = preStr;
        }
        return result;
    }

    /**
     * Adds the sign to the decimal value String.
     * @param sign Plus (true), minus (false), null of the database value not yet anonymized.
     * @param useSign Does the user want to keep the minus or plus?
     * @param result String for the whole decimal value (without sign).
     * @return String for the whole decimal value (with sign).
     */
    private static String addSign(Boolean sign, boolean useSign, String result) {
        if(useSign) {
            // Keep sign
            if(!sign) {
                result = "-" + result;
            }
        } else {
            // Random sign
            boolean isPositive = generateBoolean();
            if(!isPositive) {
                result = "-" + result;
            }
        }
        return result;
    }

    /**
     * Generates a random BigDecimal between min and max bounds.
     * @param min Minimal BigDecimal value.
     * @param max Maximal BigDecimal value.
     * @param decimalPlaces Maximal decimal place count.
     * @return Random BigDecimal.
     */
    public static BigDecimal generateBigDecimalInterval(BigDecimal min, BigDecimal max, int decimalPlaces) {
        if(max.compareTo(min) <= 0) {
            throw new IllegalArgumentException("Max <= Min!");
        }

        BigDecimal diff = max.subtract(min);                                                    // Differenz ausrechnen
        BigInteger tmpInt = (diff.movePointRight(decimalPlaces)).toBigInteger();                // Differenz * 10^decPlaces
        BigDecimal x = new BigDecimal(generateBigIntegerInterval(BigInteger.ZERO, tmpInt));     // x = rnd Wert aus Intervall
        return min.add(x.movePointLeft(decimalPlaces));                                         // min + (x * 10^-decPlaces)
    }

    /**
     * Generates a random Boolean.
     * @return Random Boolean.
     */
    public static Boolean generateBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    /**
     * Generates a random byte array with a fixed byte size.
     * @param size Byte size.
     * @return Random byte array.
     */
    public static byte[] generateBinaryFixedSize(Integer size) {
        if(size != null) {
            if(size <= 0) {
                throw new IllegalArgumentException("Size <= 0!");
            }

            byte[] byteArray = new byte[size];
            ThreadLocalRandom.current().nextBytes(byteArray);
            return byteArray;

        } else {
            return null;
        }
    }

    /**
     * Generates a random byte array that has a byte length between min and max bounds.
     * @param minBytes Minimal byte length.
     * @param maxBytes Maximal byte length.
     * @return Random byte array.
     */
    public static byte[] generateBinaryInterval(int minBytes, int maxBytes) {
        if(maxBytes < minBytes) {
            throw new IllegalArgumentException("Max < Min!");
        }

        int size;
        if(maxBytes != Integer.MAX_VALUE) {
            size = ThreadLocalRandom.current().nextInt(minBytes, maxBytes + 1);
        } else {
            size = ThreadLocalRandom.current().nextInt(minBytes, maxBytes);
        }
        return generateBinaryFixedSize(size);
    }

    /**
     * Generates a random Date between min and max bounds.
     * @param minDate Minimal date.
     * @param maxDate Maximal date.
     * @return Random date.
     */
    public static Date generateDateInterval(LocalDate minDate, LocalDate maxDate) {
        LocalDate maxDateIncr = maxDate.plusDays(1);
        long randomDay = ThreadLocalRandom.current().nextLong(minDate.toEpochDay(), maxDateIncr.toEpochDay());
        return Date.valueOf(LocalDate.ofEpochDay(randomDay));
    }

    /**
     * Generates a random Time between min and max bounds.
     * @param minTime Minimal time.
     * @param maxTime Maximal time.
     * @return Random time.
     */
    public static Time generateTimeInterval(LocalTime minTime, LocalTime maxTime) {
        LocalTime maxTimeIncr = maxTime.plusNanos(1);
        long randomNano = ThreadLocalRandom.current().nextLong(minTime.toNanoOfDay(), maxTimeIncr.toNanoOfDay());
        return Time.valueOf(LocalTime.ofNanoOfDay(randomNano));
    }

    /**
     * Generates a random Timestamp between min and max bounds.
     * @param minDateTime Minimal timestamp.
     * @param maxDateTime Maximal timestamp.
     * @return Random timestamp.
     */
    public static Timestamp generateTimestampInterval(LocalDateTime minDateTime, LocalDateTime maxDateTime) {
        Timestamp min = Timestamp.valueOf(minDateTime);
        LocalDateTime maxDateTimeIncr = maxDateTime.plusNanos(1);
        Timestamp max = Timestamp.valueOf(maxDateTimeIncr);
        long randomTimestamp = ThreadLocalRandom.current().nextLong(min.getTime(), max.getTime());
        return new Timestamp(randomTimestamp);
    }

    /**
     * Randomly picks an ENUM value.
     * @param enumList List containing all the ENUM values.
     * @return Chosen ENUM value (String).
     */
    public static String randomlyPickEnumValue(List<String> enumList) {
        return enumList.get(ThreadLocalRandom.current().nextInt(enumList.size()));
    }
}