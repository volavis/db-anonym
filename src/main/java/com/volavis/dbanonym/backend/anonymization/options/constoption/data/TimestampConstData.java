package com.volavis.dbanonym.backend.anonymization.options.constoption.data;

import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionData;

import java.time.LocalDateTime;

/**
 * Saves the (const) GUI user input for the GuiID Timestamp.
 */
public class TimestampConstData extends ConstOptionData {

    private LocalDateTime localDateTimeValue;

    @Override
    public boolean isValid() {
        return localDateTimeValue != null;
    }

    // Getter and setter
    public LocalDateTime getLocalDateTimeValue() {
        return localDateTimeValue;
    }
    public void setLocalDateTimeValue(LocalDateTime localDateTimeValue) {
        this.localDateTimeValue = localDateTimeValue;
    }
}