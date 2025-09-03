package com.volavis.dbanonym.backend.database.attributes.rdbmsunspecific;

import com.volavis.dbanonym.backend.anonymization.additionaldata.AnonymizationDataID;
import com.volavis.dbanonym.backend.anonymization.options.GuiID;
import com.volavis.dbanonym.backend.anonymization.options.constoption.data.BooleanConstData;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndValueGenerator;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.JavaDataType;

import java.util.Map;

/**
 * Attribute for Java data type Boolean.
 */
public class BooleanAttribute extends Attribute {

    /**
     * Constructor.
     *
     * @param attribute Attribute whose properties will be copied.
     */
    public BooleanAttribute(Attribute attribute) {
        super(attribute, JavaDataType.BOOLEAN, GuiID.BOOLEAN);
    }

    @Override
    public Object getConstValue() {
        BooleanConstData optionData = (BooleanConstData) getAnonymizationOptionData();
        return optionData.getBooleanValue();
    }

    @Override
    public Object getRndValue(Map<AnonymizationDataID, Object> additionalDataMap) {
        return RndValueGenerator.generateBoolean();
    }
}