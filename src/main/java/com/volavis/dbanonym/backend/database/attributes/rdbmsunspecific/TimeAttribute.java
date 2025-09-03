package com.volavis.dbanonym.backend.database.attributes.rdbmsunspecific;

import com.volavis.dbanonym.backend.anonymization.additionaldata.AnonymizationDataID;
import com.volavis.dbanonym.backend.anonymization.options.GuiID;
import com.volavis.dbanonym.backend.anonymization.options.constoption.data.TimeConstData;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndValueGenerator;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.data.TimeRndData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.JavaDataType;

import java.sql.Time;
import java.util.Map;

/**
 * Attribute for Java data type Time.
 */
public class TimeAttribute extends Attribute {

    /**
     * Constructor.
     * @param attribute Attribute whose properties will be copied.
     */
    public TimeAttribute(Attribute attribute) {
        super(attribute, JavaDataType.TIME, GuiID.TIME);
    }

    @Override
    public Object getConstValue() {
        TimeConstData optionData = (TimeConstData) getAnonymizationOptionData();
        return Time.valueOf(optionData.getLocalTimeValue());
    }

    @Override
    public Object getRndValue(Map<AnonymizationDataID, Object> additionalDataMap) {
        TimeRndData optionData = (TimeRndData) getAnonymizationOptionData();
        return RndValueGenerator.generateTimeInterval(optionData.getMinTime(), optionData.getMaxTime());
    }
}