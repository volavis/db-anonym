package com.volavis.dbanonym.backend.database.attributes.rdbmsunspecific;

import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.additionaldata.AnonymizationDataID;
import com.volavis.dbanonym.backend.anonymization.options.GuiID;
import com.volavis.dbanonym.backend.anonymization.options.constoption.data.BinaryConstData;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndValueGenerator;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.data.BinaryRndData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.JavaDataType;

import java.util.Map;

/**
 * Attribute for Java data type Byte[].
 */
public class ByteArrayAttribute extends Attribute {

    /**
     * Constructor.
     * @param attribute Attribute whose properties will be copied.
     */
    public ByteArrayAttribute(Attribute attribute) {
        super(attribute, JavaDataType.BYTEARRAY, GuiID.BINARY);
    }

    @Override
    public String getDataTypeInformation() {
        return getPrecision() + " " + StringConstants.ATTRIBUTE_INFO_BYTES;
    }

    @Override
    public Object getConstValue() {
        BinaryConstData optionData = (BinaryConstData) getAnonymizationOptionData();
        return optionData.getByteArray();
    }

    @Override
    public Object getRndValue(Map<AnonymizationDataID, Object> additionalDataMap) {
        BinaryRndData optionData = (BinaryRndData) getAnonymizationOptionData();

        switch (optionData.getBinaryDropdownElement()) {
            case SAME_BYTE_COUNT:
                Integer size = (Integer) additionalDataMap.get(AnonymizationDataID.LENGTH);
                return RndValueGenerator.generateBinaryFixedSize(size);
            case USER_DEFINED:
                return RndValueGenerator.generateBinaryInterval(optionData.getMinBytes(), optionData.getMaxBytes());
            default:
                throw new IllegalStateException();
        }
    }
}