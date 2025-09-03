package com.volavis.dbanonym.components;

import com.vaadin.flow.component.datepicker.DatePicker;

import java.util.Arrays;

/**
 * Vaadin's DatePicker in the German language.
 */
public final class GermanDatePicker extends DatePicker.DatePickerI18n {

    /**
     * Constructor.
     */
    public GermanDatePicker() {
        this.setWeek("Woche")
                .setCalendar("Kalender")
                .setClear("Löschen")
                .setToday("Heute")
                .setCancel("Abbrechen")
                .setFirstDayOfWeek(1)
                .setMonthNames(Arrays.asList("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August",
                        "September", "Oktober", "November", "Dezember"))
                .setWeekdays(Arrays.asList("Sonntag", "Montag", "Dienstag", "Mittwoch",
                        "Donnerstag", "Freitag", "Samstag"))
                .setWeekdaysShort(Arrays.asList("So", "Mo", "Di", "Mi", "Do", "Fr", "Sa"));
    }
}