package com.volavis.dbanonym.backend.anonymization.options.constoption.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionComponent;
import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionData;
import com.volavis.dbanonym.backend.anonymization.options.constoption.data.TimeConstData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;

import java.time.Duration;
import java.util.Locale;

/**
 * Builds the (const) GUI for the GuiID Time.
 */
public class TimeConstGui extends ConstOptionComponent {

    private final TimeConstData optionData;

    private final TimePicker timePicker = new TimePicker();

    /**
     * Constructor.
     * @param optionData Data class that saves the user input.
     * @param entity Entity the attribute belongs to.
     * @param attribute The GUI will be build based on the attribute's properties.
     */
    public TimeConstGui(ConstOptionData optionData, Entity entity, Attribute attribute) {
        super(entity, attribute);
        this.optionData = (TimeConstData) optionData;

        buildComponentLayout();
        binderSetup();
    }

    @Override
    protected void buildComponentLayout() {

        // TODO: Seconds and milliseconds get chopped off
        Span title = new Span(StringConstants.CONST_COMP_TIME_TITLE);

        timePicker.setWidth("500px");
        timePicker.setStep(Duration.ofMillis(1));
        timePicker.setLocale(Locale.GERMANY);

        add(title, timePicker);
    }

    @Override
    protected void binderSetup() {
        Binder<TimeConstData> binder = new Binder<>();

        bindFields(binder);
        saveValuesSetup(binder);
        loadValuesSetup(binder);
    }

    /**
     * Binds input fields.
     * @param binder Binder the fields will be bound to.
     */
    private void bindFields(Binder<TimeConstData> binder) {
        binder.forField(timePicker)
                .asRequired(StringConstants.CONST_COMP_TIME_REQUIRED)
                .bind(TimeConstData::getLocalTimeValue, TimeConstData::setLocalTimeValue);
    }

    /**
     * Defines how and when the user input will be saved.
     * @param binder Binder that manages the input fields.
     */
    private void saveValuesSetup(Binder<TimeConstData> binder) {
        timePicker.addValueChangeListener(event -> {
            if (!binder.writeBeanIfValid(optionData)) {
                optionData.setLocalTimeValue(null);
            }
        });
    }

    /**
     * Loads previous user input.
     * @param binder Binder that manages the input fields.
     */
    private void loadValuesSetup(Binder<TimeConstData> binder) {
        if (optionData.getLocalTimeValue() != null) {
            binder.readBean(optionData);
        }
    }
}