package com.volavis.dbanonym.backend.anonymization.options.constoption.data;

import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionData;

import java.time.LocalTime;

/**
 * Saves the (const) GUI user input for the GuiID Time.
 */
public class TimeConstData extends ConstOptionData {

    private LocalTime localTimeValue;

    @Override
    public boolean isValid() {
        return localTimeValue != null;
    }

    // Getter and setter
    public LocalTime getLocalTimeValue() {
        return localTimeValue;
    }
    public void setLocalTimeValue(LocalTime localTimeValue) {
        this.localTimeValue = localTimeValue;
    }
}