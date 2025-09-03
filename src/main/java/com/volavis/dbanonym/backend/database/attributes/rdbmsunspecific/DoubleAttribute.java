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
 * Attribute for Java data type Double.
 */
public class DoubleAttribute extends Attribute {

    /**
     * Constructor.
     * @param attribute Attribute whose properties will be copied.
     */
    public DoubleAttribute(Attribute attribute) {
        super(attribute, JavaDataType.DOUBLE, GuiID.DECIMAL);
    }

    @Override
    public Object getConstValue() {
        DecimalConstData optionData = (DecimalConstData) getAnonymizationOptionData();
        return optionData.getBigDecimalValue().doubleValue();
    }

    @Override
    public Object getRndValue(Map<AnonymizationDataID, Object> additionalDataMap) {
        DecimalRndData optionData = (DecimalRndData) getAnonymizationOptionData();
        return RndValueUtil.rndValueBigDecimalSwitch(additionalDataMap, optionData).doubleValue();
    }
}