package com.volavis.dbanonym.backend.database.attributes.rdbmsunspecific;

import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.additionaldata.AnonymizationDataID;
import com.volavis.dbanonym.backend.anonymization.options.GuiID;
import com.volavis.dbanonym.backend.anonymization.options.constoption.data.IntegerConstData;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndValueUtil;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.data.IntegerRndData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.JavaDataType;

import java.math.BigInteger;
import java.util.Map;

/**
 * Attribute for Java data type Integer.
 */
public class IntegerAttribute extends Attribute {

    /**
     * Constructor.
     * @param attribute Attribute whose properties will be copied.
     */
    public IntegerAttribute(Attribute attribute) {
        super(attribute, JavaDataType.INTEGER, GuiID.INTEGER);
    }

    @Override
    public String getDataTypeInformation() {
        return getPrecision() + " " + StringConstants.ATTRIBUTE_INFO_DIGITS;
    }

    @Override
    public Object getConstValue() {
        IntegerConstData optionData = (IntegerConstData) getAnonymizationOptionData();
        int tmpInt = optionData.getBigIntegerValue().intValue();
        return isSigned() ? tmpInt : String.valueOf(tmpInt);
    }

    @Override
    public Object getRndValue(Map<AnonymizationDataID, Object> additionalDataMap) {
        IntegerRndData optionData = (IntegerRndData) getAnonymizationOptionData();
        BigInteger tmpBigInteger = RndValueUtil.rndValueBigIntegerSwitch(additionalDataMap, optionData, this);
        int tmpInt = tmpBigInteger.intValue();
        return isSigned() ? tmpInt : String.valueOf(tmpInt);
    }
}