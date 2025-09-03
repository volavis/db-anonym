package com.volavis.dbanonym.backend.database.attributes.rdbmsunspecific;

import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.additionaldata.AnonymizationDataID;
import com.volavis.dbanonym.backend.anonymization.options.GuiID;
import com.volavis.dbanonym.backend.anonymization.options.constoption.data.DecimalConstData;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndValueUtil;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.data.DecimalRndData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.JavaDataType;

import java.util.Map;

/**
 * Attribute for Java data type BigDecimal.
 */
public class BigDecimalAttribute extends Attribute {

    /**
     * Constructor.
     * @param attribute Attribute whose properties will be copied.
     */
    public BigDecimalAttribute(Attribute attribute) {
        super(attribute, JavaDataType.BIGDECIMAL, GuiID.DECIMAL);
    }

    @Override
    public String getDataTypeInformation() {
        return String.format(StringConstants.ATTRIBUTE_INFO_BIGDECIMAL, getPrecision(), getScale());
    }

    @Override
    public Object getConstValue() {
        DecimalConstData optionData = (DecimalConstData) getAnonymizationOptionData();
        return optionData.getBigDecimalValue();
    }

    @Override
    public Object getRndValue(Map<AnonymizationDataID, Object> additionalDataMap) {
        DecimalRndData optionData = (DecimalRndData) getAnonymizationOptionData();
        return RndValueUtil.rndValueBigDecimalSwitch(additionalDataMap, optionData);
    }
}