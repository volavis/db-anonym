package com.volavis.dbanonym.backend.anonymization.options.rndoption.components;

import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionComponent;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionData;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.data.TimestampRndData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.components.GermanDatePicker;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * Builds the (rnd) GUI for the GuiID Timestamp.
 */
public class TimestampRndGui extends RndOptionComponent {

    private final TimestampRndData optionData;
    private final long recordCount;

    private final DateTimePicker minDateTimePicker = new DateTimePicker();
    private final DateTimePicker maxDateTimePicker = new DateTimePicker();

    /**
     * Constructor.
     * @param optionData Data class that saves the user input.
     * @param entity Entity the attribute belongs to.
     * @param attribute The GUI will be build based on the attribute's properties.
     */
    public TimestampRndGui(RndOptionData optionData, Entity entity, Attribute attribute) {
        super(entity, attribute);
        this.optionData = (TimestampRndData) optionData;
        this.recordCount = entity.getRecordCount();

        buildComponentLayout();
        binderSetup();
    }

    @Override
    protected void buildComponentLayout() {

        // TODO: Seconds and milliseconds get chopped off
        Span title = new Span(StringConstants.RND_COMP_TIMESTAMP_TITLE);

        HorizontalLayout dateTimePickerLayout = new HorizontalLayout();

        minDateTimePicker.setLabel(StringConstants.RND_COMP_TIMESTAMP_MIN_PICKER_NAME);
        minDateTimePicker.setLocale(Locale.GERMANY);
        minDateTimePicker.setStep(Duration.ofMillis(1));
        minDateTimePicker.setDatePickerI18n(new GermanDatePicker());

        maxDateTimePicker.setLabel(StringConstants.RND_COMP_TIMESTAMP_MAX_PICKER_NAME);
        maxDateTimePicker.setLocale(Locale.GERMANY);
        maxDateTimePicker.setStep(Duration.ofMillis(1));
        maxDateTimePicker.setDatePickerI18n(new GermanDatePicker());

        dateTimePickerLayout.add(minDateTimePicker, maxDateTimePicker);
        add(title, dateTimePickerLayout);
    }

    @Override
    protected void binderSetup() {
        Binder<TimestampRndData> binder = new Binder<>();

        loadValuesSetup();
        bindFields(binder);
        saveValuesSetup(binder);
    }

    /**
     * Loads previous user input.
     */
    private void loadValuesSetup() {
        // binder.readBean() does not get used because it triggers validation!
        if(optionData.getMinDateTime() != null) {
            minDateTimePicker.setValue(optionData.getMinDateTime());
        }
        if(optionData.getMaxDateTime() != null) {
            maxDateTimePicker.setValue(optionData.getMaxDateTime());
        }
    }

    /**
     * Binds input fields.
     * @param binder Binder the fields will be bound to.
     */
    private void bindFields(Binder<TimestampRndData> binder) {
        binder.forField(minDateTimePicker)
                .asRequired(StringConstants.RND_COMP_TIMESTAMP_REQUIRED_MIN)
                .bind(TimestampRndData::getMinDateTime, TimestampRndData::setMinDateTime);

        binder.forField(maxDateTimePicker)
                .asRequired(StringConstants.RND_COMP_TIMESTAMP_REQUIRED_MAX)
                .withValidator(maxDateTime -> {
                    LocalDateTime minDateTime = minDateTimePicker.getValue();
                    if(minDateTime != null) {
                        long millisBetween = ChronoUnit.MILLIS.between(minDateTime, maxDateTime);
                        return millisBetween > 0;
                    }
                    return false;
                }, StringConstants.RND_COMP_TIMESTAMP_VALIDATOR_MAX_AFTER_MIN)
                .withValidator(maxDateTime -> {
                    if(attribute.isUnique()) {
                        LocalDateTime minDateTime = minDateTimePicker.getValue();
                        if(minDateTime != null) {
                            long millisBetween = ChronoUnit.MILLIS.between(minDateTime, maxDateTime) + 1;
                            return millisBetween >= recordCount;
                        }
                        return false;
                    }
                    return true;
                }, String.format(StringConstants.RND_COMP_TIMESTAMP_VALIDATOR_UNIQUE, recordCount))
                .bind(TimestampRndData::getMaxDateTime, TimestampRndData::setMaxDateTime);
    }

    /**
     * Defines how and when the user input will be saved.
     * @param binder Binder that manages the input fields.
     */
    private void saveValuesSetup(Binder<TimestampRndData> binder) {
        minDateTimePicker.addValueChangeListener(event -> {
            if (!binder.writeBeanIfValid(optionData)) {
                optionData.setMinDateTime(null);
            }
        });

        maxDateTimePicker.addValueChangeListener(event -> {
            if (!binder.writeBeanIfValid(optionData)) {
                optionData.setMaxDateTime(null);
            }
        });
    }
}