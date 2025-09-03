package com.volavis.dbanonym.backend.anonymization.options.constoption.components;

import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.binder.Binder;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionComponent;
import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionData;
import com.volavis.dbanonym.backend.anonymization.options.constoption.data.TimestampConstData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.components.GermanDatePicker;

import java.time.Duration;
import java.util.Locale;

/**
 * Builds the (const) GUI for the GuiID Timestamp.
 */
public class TimestampConstGui extends ConstOptionComponent {

    private final TimestampConstData optionData;

    private final DateTimePicker dateTimePicker = new DateTimePicker();

    /**
     * Constructor.
     * @param optionData Data class that saves the user input.
     * @param entity Entity the attribute belongs to.
     * @param attribute The GUI will be build based on the attribute's properties.
     */
    public TimestampConstGui(ConstOptionData optionData, Entity entity, Attribute attribute) {
        super(entity, attribute);
        this.optionData = (TimestampConstData) optionData;

        buildComponentLayout();
        binderSetup();
    }

    @Override
    protected void buildComponentLayout() {

        // TODO: Seconds and milliseconds get chopped off
        Span title = new Span(StringConstants.CONST_COMP_TIMESTAMP_TITLE);

        dateTimePicker.setLocale(Locale.GERMANY);
        dateTimePicker.setStep(Duration.ofMillis(1));
        dateTimePicker.setDatePickerI18n(new GermanDatePicker());

        add(title, dateTimePicker);
    }

    @Override
    protected void binderSetup() {
        Binder<TimestampConstData> binder = new Binder<>();

        bindFields(binder);
        saveValuesSetup(binder);
        loadValuesSetup(binder);
    }

    /**
     * Binds input fields.
     * @param binder Binder the fields will be bound to.
     */
    private void bindFields(Binder<TimestampConstData> binder) {
        binder.forField(dateTimePicker)
                .asRequired(StringConstants.CONST_COMP_TIMESTAMP_REQUIRED)
                .bind(TimestampConstData::getLocalDateTimeValue, TimestampConstData::setLocalDateTimeValue);
    }

    /**
     * Defines how and when the user input will be saved.
     * @param binder Binder that manages the input fields.
     */
    private void saveValuesSetup(Binder<TimestampConstData> binder) {
        dateTimePicker.addValueChangeListener(event -> {
            if (!binder.writeBeanIfValid(optionData)) {
                optionData.setLocalDateTimeValue(null);
            }
        });
    }

    /**
     * Loads previous user input.
     * @param binder Binder that manages the input fields.
     */
    private void loadValuesSetup(Binder<TimestampConstData> binder) {
        if (optionData.getLocalDateTimeValue() != null) {
            binder.readBean(optionData);
        }
    }
}