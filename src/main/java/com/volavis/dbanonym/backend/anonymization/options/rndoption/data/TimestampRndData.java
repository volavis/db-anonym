package com.volavis.dbanonym.backend.anonymization.options.rndoption.data;

import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionData;

import java.time.LocalDateTime;

/**
 * Saves the (rnd) GUI user input for the GuiID Timestamp.
 */
public class TimestampRndData extends RndOptionData {

    private LocalDateTime minDateTime;
    private LocalDateTime maxDateTime;

    @Override
    public boolean isValid() {
        return minDateTime != null && maxDateTime != null;
    }

    // Getter and setter
    public LocalDateTime getMinDateTime() {
        return minDateTime;
    }
    public void setMinDateTime(LocalDateTime minDateTime) {
        this.minDateTime = minDateTime;
    }
    public LocalDateTime getMaxDateTime() {
        return maxDateTime;
    }
    public void setMaxDateTime(LocalDateTime maxDateTime) {
        this.maxDateTime = maxDateTime;
    }
}