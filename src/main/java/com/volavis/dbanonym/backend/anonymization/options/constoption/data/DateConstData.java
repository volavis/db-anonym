package com.volavis.dbanonym.backend.anonymization.options.constoption.data;

import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionData;

import java.time.LocalDate;

/**
 * Saves the (const) GUI user input for the GuiID Date.
 */
public class DateConstData extends ConstOptionData {

    private LocalDate localDateValue;

    @Override
    public boolean isValid() {
        return localDateValue != null;
    }

    // Getter and setter
    public LocalDate getLocalDateValue() {
        return localDateValue;
    }
    public void setLocalDateValue(LocalDate localDateValue) {
        this.localDateValue = localDateValue;
    }
}