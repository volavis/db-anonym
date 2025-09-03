package com.volavis.dbanonym.backend.database.attributes.rdbmsunspecific;

import com.volavis.dbanonym.backend.anonymization.additionaldata.AnonymizationDataID;
import com.volavis.dbanonym.backend.anonymization.options.GuiID;
import com.volavis.dbanonym.backend.anonymization.options.constoption.data.DateConstData;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndValueGenerator;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.data.DateRndData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.JavaDataType;

import java.sql.Date;
import java.util.Map;

/**
 * Attribute for Java data type Date.
 */
public class DateAttribute extends Attribute {

    /**
     * Constructor.
     * @param attribute Attribute whose properties will be copied.
     */
    public DateAttribute(Attribute attribute) {
        super(attribute, JavaDataType.DATE, GuiID.DATE);
    }

    @Override
    public Object getConstValue() {
        DateConstData optionData = (DateConstData) getAnonymizationOptionData();
        return Date.valueOf(optionData.getLocalDateValue());
    }

    @Override
    public Object getRndValue(Map<AnonymizationDataID, Object> additionalDataMap) {
        DateRndData optionData = (DateRndData) getAnonymizationOptionData();
        return RndValueGenerator.generateDateInterval(optionData.getMinDate(), optionData.getMaxDate());
    }
}