package com.volavis.dbanonym.backend.anonymization.options.constoption.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionComponent;
import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionData;
import com.volavis.dbanonym.backend.anonymization.options.constoption.data.DecimalConstData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.backend.database.jdbc.types.JdbcDecimalType;

import java.util.Locale;

/**
 * Builds the (const) GUI for the GuiID Decimal.
 */
public class DecimalConstGui extends ConstOptionComponent {

    protected final DecimalConstData optionData;

    protected final BigDecimalField decimalField = new BigDecimalField();
    private final Div warningDiv = new Div();
    private final Span roundedValueSpan = new Span();

    /**
     * Constructor.
     * @param optionData Data class that saves the user input.
     * @param entity Entity the attribute belongs to.
     * @param attribute The GUI will be build based on the attribute's properties.
     */
    public DecimalConstGui(ConstOptionData optionData, Entity entity, Attribute attribute) {
        super(entity, attribute);
        this.optionData = (DecimalConstData) optionData;

        buildComponentLayout();
        binderSetup();
    }

    @Override
    protected void buildComponentLayout() {
        Span title = new Span(StringConstants.CONST_COMP_DECIMAL_TITLE);

        // Float and double warning div
        warningDiv.setId("warning-div");
        warningDiv.setVisible(false);
        HorizontalLayout warningLayout = new HorizontalLayout();
        Span warningSpan = new Span(StringConstants.CONST_COMP_DECIMAL_WARNING_DIV_TITLE);
        warningLayout.add(new Icon(VaadinIcon.WARNING), warningSpan);
        warningDiv.add(warningLayout, roundedValueSpan);

        decimalField.setWidth("500px");
        decimalField.setLocale(Locale.GERMANY);
        decimalField.setValueChangeMode(ValueChangeMode.EAGER);

        add(title, decimalField, warningDiv);
    }

    @Override
    protected void binderSetup() {
        Binder<DecimalConstData> binder = new Binder<>();

        bindFields(binder);
        saveValuesSetup(binder);
        loadValuesSetup(binder);
    }

    /**
     * Binds input fields.
     * @param binder Binder the fields will be bound to.
     */
    protected void bindFields(Binder<DecimalConstData> binder) {
        binder.forField(decimalField)
                .asRequired(StringConstants.CONST_COMP_DECIMAL_REQUIRED)
                .withValidator((bigDecimal, valueContext) -> {
                    switch(attribute.getJavaDataType()) {
                        case FLOAT:
                            float fValue = bigDecimal.floatValue();
                            if(fValue != Float.NEGATIVE_INFINITY && fValue != Float.POSITIVE_INFINITY) {
                                return ValidationResult.ok();
                            } else {
                                return ValidationResult.error(StringConstants.CONST_COMP_DECIMAL_VALIDATOR_INFINITY);
                            }
                        case DOUBLE:
                            double dValue = bigDecimal.doubleValue();
                            if(dValue != Double.NEGATIVE_INFINITY && dValue != Double.POSITIVE_INFINITY) {
                                return ValidationResult.ok();
                            } else {
                                return ValidationResult.error(StringConstants.CONST_COMP_DECIMAL_VALIDATOR_INFINITY);
                            }
                        case BIGDECIMAL:
                            if(JdbcDecimalType.checkPreAndPostDecimalPlaces(attribute, bigDecimal)) {
                                return ValidationResult.ok();
                            } else {
                                return ValidationResult.error(String.format(StringConstants.CONST_COMP_DECIMAL_VALIDATOR_DECIMAL_PLACES,
                                        attribute.getPrecision(), attribute.getScale()));
                            }
                    }
                    return ValidationResult.ok();
                })
                .bind(DecimalConstData::getBigDecimalValue, DecimalConstData::setBigDecimalValue);
    }

    /**
     * Defines how and when the user input will be saved.
     * @param binder Binder that manages the input fields.
     */
    protected void saveValuesSetup(Binder<DecimalConstData> binder) {
        decimalField.addValueChangeListener(event -> {
            if (!binder.writeBeanIfValid(optionData)) {
                optionData.setBigDecimalValue(null);
            }

            updateWarningDiv();
        });
    }

    /**
     * Updates the warning Div for Float and Double.
     */
    private void updateWarningDiv() {
        switch (attribute.getJavaDataType()) {
            case FLOAT:
                warningDiv.setVisible(true);
                if(decimalField.getValue() != null) {
                    roundedValueSpan.setText(StringConstants.CONST_COMP_DECIMAL_WARNING_DIV_FLOAT + decimalField.getValue().floatValue());
                }
                break;
            case DOUBLE:
                warningDiv.setVisible(true);
                if(decimalField.getValue() != null) {
                    roundedValueSpan.setText(StringConstants.CONST_COMP_DECIMAL_WARNING_DIV_DOUBLE + decimalField.getValue().doubleValue());
                }
                break;
            default:
                warningDiv.setVisible(false);
                break;
        }
    }

    /**
     * Loads previous user input.
     * @param binder Binder that manages the input fields.
     */
    private void loadValuesSetup(Binder<DecimalConstData> binder) {
        if (optionData.getBigDecimalValue() != null) {
            binder.readBean(optionData);
        }
    }
}