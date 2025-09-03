package com.volavis.dbanonym.backend.database.attributes.rdbmsunspecific;

import com.volavis.dbanonym.backend.anonymization.additionaldata.AnonymizationDataID;
import com.volavis.dbanonym.backend.anonymization.options.GuiID;
import com.volavis.dbanonym.backend.anonymization.options.constoption.data.TimestampConstData;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndValueGenerator;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.data.TimestampRndData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.JavaDataType;

import java.sql.Timestamp;
import java.util.Map;

/**
 * Attribute for Java data type Timestamp.
 */
public class TimestampAttribute extends Attribute {

    /**
     * Constructor.
     * @param attribute Attribute whose properties will be copied.
     */
    public TimestampAttribute(Attribute attribute) {
        super(attribute, JavaDataType.TIMESTAMP, GuiID.TIMESTAMP);
    }

    @Override
    public Object getConstValue() {
        TimestampConstData optionData = (TimestampConstData) getAnonymizationOptionData();
        return Timestamp.valueOf(optionData.getLocalDateTimeValue());
    }

    @Override
    public Object getRndValue(Map<AnonymizationDataID, Object> additionalDataMap) {
        TimestampRndData optionData = (TimestampRndData) getAnonymizationOptionData();
        return RndValueGenerator.generateTimestampInterval(optionData.getMinDateTime(), optionData.getMaxDateTime());
    }
}