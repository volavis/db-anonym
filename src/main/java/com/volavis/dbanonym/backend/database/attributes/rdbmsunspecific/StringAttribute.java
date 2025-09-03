package com.volavis.dbanonym.backend.database.attributes.rdbmsunspecific;

import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.additionaldata.AnonymizationDataID;
import com.volavis.dbanonym.backend.anonymization.options.GuiID;
import com.volavis.dbanonym.backend.anonymization.options.constoption.data.CharactersConstData;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndValueGenerator;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.data.CharactersRndData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.JavaDataType;

import java.util.Map;

/**
 * Attribute for Java data type String.
 */
public class StringAttribute extends Attribute {

    /**
     * Constructor.
     * @param attribute Attribute whose properties will be copied.
     */
    public StringAttribute(Attribute attribute) {
        super(attribute, JavaDataType.STRING, GuiID.CHARACTERS);
    }

    @Override
    public String getDataTypeInformation() {
        return getPrecision() + " " + StringConstants.ATTRIBUTE_INFO_CHARS;
    }

    @Override
    public Object getConstValue() {
        CharactersConstData optionData = (CharactersConstData) getAnonymizationOptionData();
        return optionData.getStringValue();
    }

    @Override
    public Object getRndValue(Map<AnonymizationDataID, Object> additionalDataMap) {
        CharactersRndData optionData = (CharactersRndData) getAnonymizationOptionData();

        // Problem unique: Attribute can be case insensitive! Only uses lower case letters!
        switch (optionData.getCharactersDropdownElement()) {
            case SAME_LENGTH:
                Integer length = (Integer) additionalDataMap.get(AnonymizationDataID.LENGTH);
                if(isUnique()) {
                    return RndValueGenerator.uniqueGenerateStringFixedLength(length, true, true);
                } else {
                    return RndValueGenerator.generateStringFixedLength(length, true, true);
                }
            case USER_DEFINED:
                if(isUnique()) {
                    return RndValueGenerator.uniqueGenerateStringInterval(optionData.getMinLength(),
                            optionData.getMaxLength(), true, true);
                } else {
                    return RndValueGenerator.generateStringInterval(optionData.getMinLength(),
                            optionData.getMaxLength(), true, true);
                }
            default:
                throw new IllegalStateException();
        }
    }
}