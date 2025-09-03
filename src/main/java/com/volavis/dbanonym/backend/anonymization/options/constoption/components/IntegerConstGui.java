package com.volavis.dbanonym.backend.anonymization.options.constoption.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigIntegerConverter;
import com.vaadin.flow.data.validator.BigIntegerRangeValidator;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionComponent;
import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionData;
import com.volavis.dbanonym.backend.anonymization.options.constoption.data.IntegerConstData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.backend.database.jdbc.types.JdbcIntegerType;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Builds the (const) GUI for the GuiID Integer.
 */
public class IntegerConstGui extends ConstOptionComponent {

    private final IntegerConstData optionData;

    private final TextField textField = new TextField();

    /**
     * Constructor.
     * @param optionData Data class that saves the user input.
     * @param entity Entity the attribute belongs to.
     * @param attribute The GUI will be build based on the attribute's properties.
     */
    public IntegerConstGui(ConstOptionData optionData, Entity entity, Attribute attribute) {
        super(entity, attribute);
        this.optionData = (IntegerConstData) optionData;

        buildComponentLayout();
        binderSetup();
    }

    @Override
    protected void buildComponentLayout() {
        Span title = new Span(StringConstants.CONST_COMP_INTEGER_TITLE);

        textField.setWidth("500px");
        textField.setValueChangeMode(ValueChangeMode.EAGER);

        add(title, textField);
    }

    @Override
    protected void binderSetup() {
        Binder<IntegerConstData> binder = new Binder<>();

        bindFields(binder);
        saveValuesSetup(binder);
        loadValuesSetup(binder);
    }

    /**
     * Binds input fields.
     * @param binder Binder the fields will be bound to.
     */
    private void bindFields(Binder<IntegerConstData> binder) {
        binder.forField(textField)
                .asRequired(StringConstants.CONST_COMP_INTEGER_REQUIRED)
                .withValidator(new RegexpValidator(StringConstants.CONST_COMP_INTEGER_VALIDATOR_NAN,
                        "^\\s*[-]?\\d+\\s*"))
                .withConverter(new StringToBigIntegerConverter(StringConstants.CONST_COMP_INTEGER_VALIDATOR_NAN) {
                    @Override
                    protected java.text.NumberFormat getFormat(Locale locale) {
                        NumberFormat format = super.getFormat(Locale.GERMANY);  // Locale
                        format.setGroupingUsed(false);                          // Separator disabled!
                        return format;
                    }
                })
                .withValidator(new BigIntegerRangeValidator(StringConstants.CONST_COMP_INTEGER_VALIDATOR_RANGE,
                        JdbcIntegerType.getMinOrMax(true, attribute.getJdbcDataTypeValidation(), attribute.isSigned()),
                        JdbcIntegerType.getMinOrMax(false, attribute.getJdbcDataTypeValidation(), attribute.isSigned())))
                .bind(IntegerConstData::getBigIntegerValue, IntegerConstData::setBigIntegerValue);
    }

    /**
     * Defines how and when the user input will be saved.
     * @param binder Binder that manages the input fields.
     */
    private void saveValuesSetup(Binder<IntegerConstData> binder) {
        textField.addValueChangeListener(event -> {
            if (!binder.writeBeanIfValid(optionData)) {
                optionData.setBigIntegerValue(null);
            }
        });
    }

    /**
     * Loads previous user input.
     * @param binder Binder that manages the input fields.
     */
    private void loadValuesSetup(Binder<IntegerConstData> binder) {
        if (optionData.getBigIntegerValue() != null) {
            binder.readBean(optionData);
        }
    }
}