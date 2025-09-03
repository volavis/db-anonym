package com.volavis.dbanonym.backend.anonymization.options.rndoption.data;

import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionData;

import java.time.LocalDate;

/**
 * Saves the (rnd) GUI user input for the GuiID Date.
 */
public class DateRndData extends RndOptionData {

    private LocalDate minDate;
    private LocalDate maxDate;

    @Override
    public boolean isValid() {
        return minDate != null && maxDate != null;
    }

    // Getter and setter
    public LocalDate getMinDate() {
        return minDate;
    }
    public void setMinDate(LocalDate minDate) {
        this.minDate = minDate;
    }
    public LocalDate getMaxDate() {
        return maxDate;
    }
    public void setMaxDate(LocalDate maxDate) {
        this.maxDate = maxDate;
    }
}