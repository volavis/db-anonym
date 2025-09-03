package com.volavis.dbanonym.backend.anonymization.options.constoption.components;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.binder.Binder;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionComponent;
import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionData;
import com.volavis.dbanonym.backend.anonymization.options.constoption.data.DateConstData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.components.GermanDatePicker;

import java.util.Locale;

/**
 * Builds the (const) GUI for the GuiID Date.
 */
public class DateConstGui extends ConstOptionComponent {

    private final DateConstData optionData;

    private final DatePicker datePicker = new DatePicker();

    /**
     * Constructor.
     * @param optionData Data class that saves the user input.
     * @param entity Entity the attribute belongs to.
     * @param attribute The GUI will be build based on the attribute's properties.
     */
    public DateConstGui(ConstOptionData optionData, Entity entity, Attribute attribute) {
        super(entity, attribute);
        this.optionData = (DateConstData) optionData;

        buildComponentLayout();
        binderSetup();
    }

    @Override
    protected void buildComponentLayout() {
        Span title = new Span(StringConstants.CONST_COMP_DATE_TITLE);

        datePicker.setLocale(Locale.GERMANY);
        datePicker.setI18n(new GermanDatePicker());

        add(title, datePicker);
    }

    @Override
    protected void binderSetup() {
        Binder<DateConstData> binder = new Binder<>();

        bindFields(binder);
        saveValuesSetup(binder);
        loadValuesSetup(binder);
    }

    /**
     * Binds input fields.
     * @param binder Binder the fields will be bound to.
     */
    private void bindFields(Binder<DateConstData> binder) {
        binder.forField(datePicker)
                .asRequired(StringConstants.CONST_COMP_DATE_REQUIRED)
                .bind(DateConstData::getLocalDateValue, DateConstData::setLocalDateValue);
    }

    /**
     * Defines how and when the user input will be saved.
     * @param binder Binder that manages the input fields.
     */
    private void saveValuesSetup(Binder<DateConstData> binder) {
        datePicker.addValueChangeListener(event -> {
            if (!binder.writeBeanIfValid(optionData)) {
                optionData.setLocalDateValue(null);
            }
        });
    }

    /**
     * Loads previous user input.
     * @param binder Binder that manages the input fields.
     */
    private void loadValuesSetup(Binder<DateConstData> binder) {
        if (optionData.getLocalDateValue() != null) {
            binder.readBean(optionData);
        }
    }
}