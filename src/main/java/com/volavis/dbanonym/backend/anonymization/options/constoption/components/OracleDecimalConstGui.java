package com.volavis.dbanonym.backend.anonymization.options.constoption.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionData;
import com.volavis.dbanonym.backend.anonymization.options.constoption.data.DecimalConstData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.backend.database.jdbc.types.JdbcDecimalType;

import java.util.Locale;

/**
 * Builds the (const) GUI for the GuiID Oracle_Decimal.
 */
public class OracleDecimalConstGui extends DecimalConstGui {

    /**
     * Constructor.
     * @param optionData Data class that saves the user input.
     * @param entity     Entity the attribute belongs to.
     * @param attribute  The GUI will be build based on the attribute's properties.
     */
    public OracleDecimalConstGui(ConstOptionData optionData, Entity entity, Attribute attribute) {
        super(optionData, entity, attribute);
    }

    @Override
    protected void buildComponentLayout() {
        Span title = new Span(StringConstants.CONST_COMP_DECIMAL_TITLE);

        decimalField.setWidth("500px");
        decimalField.setLocale(Locale.GERMANY);
        decimalField.setValueChangeMode(ValueChangeMode.EAGER);

        add(title, decimalField);
    }

    /**
     * Binds input fields.
     * @param binder Binder the fields will be bound to.
     */
    @Override
    protected void bindFields(Binder<DecimalConstData> binder) {
        binder.forField(decimalField)
                .asRequired(StringConstants.CONST_COMP_DECIMAL_REQUIRED)
                .withValidator(bigDecimal -> JdbcDecimalType.oracleCheckPreAndPostDecimalPlaces(attribute, bigDecimal),
                        String.format(StringConstants.CONST_COMP_ORACLE_DECIMAL_VALIDATOR_DECIMAL_PLACES,
                        attribute.getPrecision(), attribute.getScale()))
                .bind(DecimalConstData::getBigDecimalValue, DecimalConstData::setBigDecimalValue);
    }

    /**
     * Defines how and when the user input will be saved.
     * @param binder Binder that manages the input fields.
     */
    @Override
    protected void saveValuesSetup(Binder<DecimalConstData> binder) {
        decimalField.addValueChangeListener(event -> {
            if (!binder.writeBeanIfValid(optionData)) {
                optionData.setBigDecimalValue(null);
            }
        });
    }
}