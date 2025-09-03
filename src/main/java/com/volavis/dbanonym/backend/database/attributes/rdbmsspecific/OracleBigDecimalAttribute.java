package com.volavis.dbanonym.backend.database.attributes.rdbmsspecific;

import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.GuiID;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.JavaDataType;
import com.volavis.dbanonym.backend.database.attributes.rdbmsunspecific.BigDecimalAttribute;
import com.volavis.dbanonym.backend.database.jdbc.types.JdbcDecimalType;

/**
 * Attribute for Oracle's numeric data types.
 */
public class OracleBigDecimalAttribute extends BigDecimalAttribute {

    /**
     * Constructor.
     * @param attribute Attribute whose properties will be copied.
     */
    public OracleBigDecimalAttribute(Attribute attribute) {
        super(attribute);
        setJavaDataType(JavaDataType.ORACLE_BIGDECIMAL);
        setGuiID(GuiID.ORACLE_DECIMAL);
    }

    @Override
    public String getDataTypeInformation() {
        if(getScale() == JdbcDecimalType.STORE_VALUES_AS_GIVEN_SCALE) {
            return String.format(StringConstants.ATTRIBUTE_INFO_BIGDECIMAL_ANY_SCALE, getPrecision());
        } else {
            return String.format(StringConstants.ATTRIBUTE_INFO_BIGDECIMAL, getPrecision(), getScale());
        }
    }
}