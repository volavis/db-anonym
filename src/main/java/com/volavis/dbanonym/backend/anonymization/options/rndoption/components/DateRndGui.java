package com.volavis.dbanonym.backend.anonymization.options.rndoption.components;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionComponent;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionData;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.data.DateRndData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.components.GermanDatePicker;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * Builds the (rnd) GUI for the GuiID Date.
 */
public class DateRndGui extends RndOptionComponent {

    private final DateRndData optionData;
    private final long recordCount;

    private final DatePicker minDatePicker = new DatePicker();
    private final DatePicker maxDatePicker = new DatePicker();

    /**
     * Constructor.
     * @param optionData Data class that saves the user input.
     * @param entity Entity the attribute belongs to.
     * @param attribute The GUI will be build based on the attribute's properties.
     */
    public DateRndGui(RndOptionData optionData, Entity entity, Attribute attribute) {
        super(entity, attribute);
        this.optionData = (DateRndData) optionData;
        this.recordCount = entity.getRecordCount();

        buildComponentLayout();
        binderSetup();
    }

    @Override
    protected void buildComponentLayout() {
        Span title = new Span(StringConstants.RND_COMP_DATE_TITLE);

        HorizontalLayout datePickerLayout = new HorizontalLayout();

        minDatePicker.setLabel(StringConstants.RND_COMP_DATE_MIN_PICKER_NAME);
        minDatePicker.setLocale(Locale.GERMANY);
        minDatePicker.setI18n(new GermanDatePicker());

        maxDatePicker.setLabel(StringConstants.RND_COMP_DATE_MAX_PICKER_NAME);
        maxDatePicker.setLocale(Locale.GERMANY);
        maxDatePicker.setI18n(new GermanDatePicker());

        datePickerLayout.add(minDatePicker, maxDatePicker);
        add(title, datePickerLayout);
    }

    @Override
    protected void binderSetup() {
        Binder<DateRndData> binder = new Binder<>();

        loadValuesSetup();
        bindFields(binder);
        saveValuesSetup(binder);
    }

    /**
     * Loads previous user input.
     */
    private void loadValuesSetup() {
        // binder.readBean() does not get used because it triggers validation!
        if(optionData.getMinDate() != null) {
            minDatePicker.setValue(optionData.getMinDate());
        }
        if(optionData.getMaxDate() != null) {
            maxDatePicker.setValue(optionData.getMaxDate());
        }
    }

    /**
     * Binds input fields.
     * @param binder Binder the fields will be bound to.
     */
    private void bindFields(Binder<DateRndData> binder) {
        binder.forField(minDatePicker)
                .asRequired(StringConstants.RND_COMP_DATE_REQUIRED_MIN)
                .bind(DateRndData::getMinDate, DateRndData::setMinDate);

        binder.forField(maxDatePicker)
                .asRequired(StringConstants.RND_COMP_DATE_REQUIRED_MAX)
                .withValidator(maxDate -> {
                    LocalDate minDate = minDatePicker.getValue();
                    if(minDate != null) {
                        long daysBetween = ChronoUnit.DAYS.between(minDate, maxDate);
                        return daysBetween > 0;
                    }
                    return false;
                }, StringConstants.RND_COMP_DATE_VALIDATOR_MAX_AFTER_MIN)
                .withValidator(maxDate -> {
                    if(attribute.isUnique()) {
                        LocalDate minDate = minDatePicker.getValue();
                        if(minDate != null) {
                            long daysBetween = ChronoUnit.DAYS.between(minDate, maxDate) + 1;
                            return daysBetween >= recordCount;
                        }
                        return false;
                    }
                    return true;
                }, String.format(StringConstants.RND_COMP_DATE_VALIDATOR_UNIQUE, recordCount))
                .bind(DateRndData::getMaxDate, DateRndData::setMaxDate);
    }

    /**
     * Defines how and when the user input will be saved.
     * @param binder Binder that manages the input fields.
     */
    private void saveValuesSetup(Binder<DateRndData> binder) {
        minDatePicker.addValueChangeListener(event -> {
            if (!binder.writeBeanIfValid(optionData)) {
                optionData.setMinDate(null);
            }
        });

        maxDatePicker.addValueChangeListener(event -> {
            if (!binder.writeBeanIfValid(optionData)) {
                optionData.setMaxDate(null);
            }
        });
    }
}