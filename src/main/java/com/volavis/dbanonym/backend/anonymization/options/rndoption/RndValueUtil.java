package com.volavis.dbanonym.backend.anonymization.options.rndoption;

import com.volavis.dbanonym.backend.anonymization.additionaldata.AnonymizationDataID;
import com.volavis.dbanonym.backend.anonymization.additionaldata.listbuilder.DecimalData;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.data.DecimalRndData;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.data.IntegerRndData;
import com.volavis.dbanonym.backend.database.Attribute;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 * Contains static methods used in some attribute's getRndValue() method.
 */
public final class RndValueUtil {

    /**
     * Constructor.
     */
    private RndValueUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * getRndValue(): Switch statement for GuiID Integer.
     * @param additionalDataMap Map containing additional metadata used in anonymization.
     * @param optionData The attribute's RndOptionData subclass.
     * @param attribute The attribute that will be anonymized.
     * @return Random BigInteger.
     */
    public static BigInteger rndValueBigIntegerSwitch(Map<AnonymizationDataID, Object> additionalDataMap,
                                                      IntegerRndData optionData, Attribute attribute) {
        BigInteger tmpBigInteger;
        switch (optionData.getIntegerDropdownElement()) {
            case SAME_DIGIT_COUNT:
                Integer length = (Integer) additionalDataMap.get(AnonymizationDataID.LENGTH);
                Boolean sign = (Boolean) additionalDataMap.get(AnonymizationDataID.SIGN);
                tmpBigInteger = RndValueGenerator.generateBigIntegerFixedLength(
                        attribute.getJdbcDataTypeValidation(), attribute.isSigned(),
                        length, sign, optionData.useSign());
                break;
            case VALUE_RANGE:
                tmpBigInteger = RndValueGenerator.generateBigIntegerValueRange(
                        attribute.getJdbcDataTypeValidation(), attribute.isSigned());
                break;
            case USER_DEFINED:
                tmpBigInteger = RndValueGenerator.generateBigIntegerInterval(optionData.getMinInteger(),
                        optionData.getMaxInteger());
                break;
            default:
                throw new IllegalStateException();
        }
        return tmpBigInteger;
    }

    /**
     * getRndValue(): Switch statement for GuiID Decimal.
     * @param additionalDataMap Map containing additional metadata used in anonymization.
     * @param optionData The attribute's RndOptionData subclass.
     * @return Random BigDecimal.
     */
    public static BigDecimal rndValueBigDecimalSwitch(Map<AnonymizationDataID, Object> additionalDataMap,
                                                               DecimalRndData optionData) {
        BigDecimal tmpBigDecimal;
        switch (optionData.getDecimalDropdownElement()) {
            case SAME_PRE_POST_DECIMAL_PLACES:
                DecimalData decimalData = (DecimalData) additionalDataMap.get(AnonymizationDataID.DECIMAL);
                tmpBigDecimal = RndValueGenerator.generateBigDecimalFixedLength(decimalData.getPreLength(),
                        decimalData.getPostLength(), decimalData.getSign(), optionData.useSign());
                break;
            case USER_DEFINED:
                tmpBigDecimal = RndValueGenerator.generateBigDecimalInterval(optionData.getMinDecimal(),
                        optionData.getMaxDecimal(), optionData.getDecimalPlace());
                break;
            default:
                throw new IllegalStateException();
        }
        return tmpBigDecimal;
    }
}