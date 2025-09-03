package com.volavis.dbanonym.backend.anonymization.options.constoption.data;

import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionData;

/**
 * Saves the (const) GUI user input for the GuiID Boolean.
 */
public class BooleanConstData extends ConstOptionData {

    private Boolean booleanValue;

    @Override
    public boolean isValid() {
        return booleanValue != null;
    }

    // Getter and setter
    public Boolean getBooleanValue() {
        return booleanValue;
    }
    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }
}