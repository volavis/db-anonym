package com.volavis.dbanonym.backend.anonymization.options.constoption.data;

import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionData;

/**
 * Saves the (const) GUI user input for the GuiID Mysql_Enum.
 */
public class MysqlEnumConstData extends ConstOptionData {

    private String enumValue;

    @Override
    public boolean isValid() {
        return enumValue != null;
    }

    // Getter and setter
    public String getEnumValue() {
        return enumValue;
    }
    public void setEnumValue(String enumValue) {
        this.enumValue = enumValue;
    }
}