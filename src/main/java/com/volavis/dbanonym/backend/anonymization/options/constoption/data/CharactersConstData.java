package com.volavis.dbanonym.backend.anonymization.options.constoption.data;

import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionData;

/**
 * Saves the (const) GUI user input for the GuiID Characters.
 */
public class CharactersConstData extends ConstOptionData {

    private String stringValue;

    @Override
    public boolean isValid() {
        return stringValue != null;
    }

    // Getter and setter
    public String getStringValue() {
        return stringValue;
    }
    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}