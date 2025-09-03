package com.volavis.dbanonym.backend.database.attributes.rdbmsunspecific;

import com.volavis.dbanonym.backend.anonymization.additionaldata.AnonymizationDataID;
import com.volavis.dbanonym.backend.anonymization.options.GuiID;
import com.volavis.dbanonym.backend.anonymization.options.constoption.data.DecimalConstData;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndValueUtil;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.data.DecimalRndData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.JavaDataType;

import java.util.Map;

/**
 * Attribute for Java data type Float.
 */
public class FloatAttribute extends Attribute {

    /**
     * Constructor.
     * @param attribute Attribute whose properties will be copied.
     */
    public FloatAttribute(Attribute attribute) {
        super(attribute, JavaDataType.FLOAT, GuiID.DECIMAL);
    }

    @Override
    public Object getConstValue() {
        DecimalConstData optionData = (DecimalConstData) getAnonymizationOptionData();
        return optionData.getBigDecimalValue().floatValue();
    }

    @Override
    public Object getRndValue(Map<AnonymizationDataID, Object> additionalDataMap) {
        DecimalRndData optionData = (DecimalRndData) getAnonymizationOptionData();
        return RndValueUtil.rndValueBigDecimalSwitch(additionalDataMap, optionData).floatValue();
    }
}