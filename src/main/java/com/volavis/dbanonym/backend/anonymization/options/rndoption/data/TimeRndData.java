package com.volavis.dbanonym.backend.anonymization.options.rndoption.data;

import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionData;

import java.time.LocalTime;

/**
 * Saves the (rnd) GUI user input for the GuiID Time.
 */
public class TimeRndData extends RndOptionData {

    private LocalTime minTime;
    private LocalTime maxTime;

    @Override
    public boolean isValid() {
        return minTime != null && maxTime != null;
    }

    // Getter and setter
    public LocalTime getMinTime() {
        return minTime;
    }
    public void setMinTime(LocalTime minTime) {
        this.minTime = minTime;
    }
    public LocalTime getMaxTime() {
        return maxTime;
    }
    public void setMaxTime(LocalTime maxTime) {
        this.maxTime = maxTime;
    }
}