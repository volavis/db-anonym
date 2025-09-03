package com.volavis.dbanonym.backend.anonymization.options.constoption.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionComponent;
import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionData;
import com.volavis.dbanonym.backend.anonymization.options.constoption.data.BinaryConstData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;

import java.nio.charset.StandardCharsets;

/**
 * Builds the (const) GUI for the GuiID Binary.
 */
public class BinaryConstGui extends ConstOptionComponent {

    private final BinaryConstData optionData;

    private final TextField textField = new TextField();

    /**
     * Constructor.
     * @param optionData Data class that saves the user input.
     * @param entity Entity the attribute belongs to.
     * @param attribute The GUI will be build based on the attribute's properties.
     */
    public BinaryConstGui(ConstOptionData optionData, Entity entity, Attribute attribute) {
        super(entity, attribute);
        this.optionData = (BinaryConstData) optionData;

        buildComponentLayout();
        binderSetup();
    }

    @Override
    protected void buildComponentLayout() {
        Span title = new Span(StringConstants.CONST_COMP_BINARY_TITLE);

        textField.setWidth("500px");
        textField.setValueChangeMode(ValueChangeMode.EAGER);

        add(title, textField);
    }

    @Override
    protected void binderSetup() {
        Binder<BinaryConstData> binder = new Binder<>();

        bindFields(binder);
        saveValuesSetup(binder);
        loadValuesSetup(binder);
    }

    /**
     * Binds input fields.
     * @param binder Binder the fields will be bound to.
     */
    private void bindFields(Binder<BinaryConstData> binder) {
        binder.forField(textField)
                .asRequired(StringConstants.CONST_COMP_BINARY_REQUIRED)
                .withValidator(text -> text.getBytes(StandardCharsets.UTF_8).length <= attribute.getPrecision(),
                        String.format(StringConstants.CONST_COMP_BINARY_VALIDATOR_LENGTH,  attribute.getPrecision()))
                .bind(BinaryConstData::getStringFromByteArray, BinaryConstData::setByteArrayFromString);
    }

    /**
     * Defines how and when the user input will be saved.
     * @param binder Binder that manages the input fields.
     */
    private void saveValuesSetup(Binder<BinaryConstData> binder) {
        textField.addValueChangeListener(event -> {
            if (!binder.writeBeanIfValid(optionData)) {
                optionData.setByteArrayFromString(null);
            }
        });
    }

    /**
     * Loads previous user input.
     * @param binder Binder that manages the input fields.
     */
    private void loadValuesSetup(Binder<BinaryConstData> binder) {
        if (optionData.getByteArray() != null) {
            binder.readBean(optionData);
        }
    }
}