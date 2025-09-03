package com.volavis.dbanonym.backend.anonymization.options.rndoption.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionComponent;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionData;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.data.TimeRndData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * Builds the (rnd) GUI for the GuiID Time.
 */
public class TimeRndGui extends RndOptionComponent {

    private final TimeRndData optionData;
    private final long recordCount;

    private final TimePicker minTimePicker = new TimePicker();
    private final TimePicker maxTimePicker = new TimePicker();

    /**
     * Constructor.
     * @param optionData Data class that saves the user input.
     * @param entity Entity the attribute belongs to.
     * @param attribute The GUI will be build based on the attribute's properties.
     */
    public TimeRndGui(RndOptionData optionData, Entity entity, Attribute attribute) {
        super(entity, attribute);
        this.optionData = (TimeRndData) optionData;
        this.recordCount = entity.getRecordCount();

        buildComponentLayout();
        binderSetup();
    }

    @Override
    protected void buildComponentLayout() {

        // TODO: Seconds and milliseconds get chopped off!
        Span title = new Span(StringConstants.RND_COMP_TIME_TITLE);

        HorizontalLayout timePickerLayout = new HorizontalLayout();

        minTimePicker.setLabel(StringConstants.RND_COMP_TIME_MIN_PICKER_NAME);
        minTimePicker.setStep(Duration.ofMillis(1));
        minTimePicker.setLocale(Locale.GERMANY);

        maxTimePicker.setLabel(StringConstants.RND_COMP_TIME_MAX_PICKER_NAME);
        maxTimePicker.setStep(Duration.ofMillis(1));
        maxTimePicker.setLocale(Locale.GERMANY);

        timePickerLayout.add(minTimePicker, maxTimePicker);
        add(title, timePickerLayout);
    }

    @Override
    protected void binderSetup() {
        Binder<TimeRndData> binder = new Binder<>();

        loadValuesSetup();
        bindFields(binder);
        saveValuesSetup(binder);
    }

    /**
     * Loads previous user input.
     */
    private void loadValuesSetup() {
        // binder.readBean() does not get used because it triggers validation!
        if(optionData.getMinTime() != null) {
            minTimePicker.setValue(optionData.getMinTime());
        }
        if(optionData.getMaxTime() != null) {
            maxTimePicker.setValue(optionData.getMaxTime());
        }
    }

    /**
     * Binds input fields.
     * @param binder Binder the fields will be bound to.
     */
    private void bindFields(Binder<TimeRndData> binder) {
        binder.forField(minTimePicker)
                .asRequired(StringConstants.RND_COMP_TIME_REQUIRED_MIN)
                .bind(TimeRndData::getMinTime, TimeRndData::setMinTime);

        binder.forField(maxTimePicker)
                .asRequired(StringConstants.RND_COMP_TIME_REQUIRED_MAX)
                .withValidator(maxTime -> {
                    LocalTime minTime = minTimePicker.getValue();
                    if(minTime != null) {
                        long millisBetween = ChronoUnit.MILLIS.between(minTime, maxTime);
                        return millisBetween > 0;
                    }
                    return false;
                }, StringConstants.RND_COMP_TIME_VALIDATOR_MAX_AFTER_MIN)
                .withValidator(maxTime -> {
                    if(attribute.isUnique()) {
                        LocalTime minTime = minTimePicker.getValue();
                        if(minTime != null) {
                            long millisBetween = ChronoUnit.MILLIS.between(minTime, maxTime) + 1;
                            return millisBetween >= recordCount;
                        }
                        return false;
                    }
                    return true;
                }, String.format(StringConstants.RND_COMP_TIME_VALIDATOR_UNIQUE, recordCount))
                .bind(TimeRndData::getMaxTime, TimeRndData::setMaxTime);
    }

    /**
     * Defines how and when the user input will be saved.
     * @param binder Binder that manages the input fields.
     */
    private void saveValuesSetup(Binder<TimeRndData> binder) {
        minTimePicker.addValueChangeListener(event -> {
            if (!binder.writeBeanIfValid(optionData)) {
                optionData.setMinTime(null);
            }
        });

        maxTimePicker.addValueChangeListener(event -> {
            if (!binder.writeBeanIfValid(optionData)) {
                optionData.setMaxTime(null);
            }
        });
    }
}