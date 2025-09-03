package com.volavis.dbanonym.backend.anonymization.options.constoption.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionComponent;
import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionData;
import com.volavis.dbanonym.backend.anonymization.options.constoption.data.CharactersConstData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;

/**
 * Builds the (const) GUI for the GuiID Characters.
 */
public class CharactersConstGui extends ConstOptionComponent {

    protected final CharactersConstData optionData;

    protected final TextField textField = new TextField();

    /**
     * Constructor.
     * @param optionData Data class that saves the user input.
     * @param entity Entity the attribute belongs to.
     * @param attribute The GUI will be build based on the attribute's properties.
     */
    public CharactersConstGui(ConstOptionData optionData, Entity entity, Attribute attribute) {
        super(entity, attribute);
        this.optionData = (CharactersConstData) optionData;

        buildComponentLayout();
        binderSetup();
    }

    @Override
    protected void buildComponentLayout() {
        Span title = new Span(StringConstants.CONST_COMP_CHARACTERS_TITLE);

        textField.setWidth("500px");
        textField.setValueChangeMode(ValueChangeMode.EAGER);

        add(title, textField);
    }

    @Override
    protected void binderSetup() {
        Binder<CharactersConstData> binder = new Binder<>();

        bindFields(binder);
        saveValuesSetup(binder);
        loadValuesSetup(binder);
    }

    /**
     * Binds input fields.
     * @param binder Binder the fields will be bound to.
     */
    protected void bindFields(Binder<CharactersConstData> binder) {
        binder.forField(textField)
                .asRequired(StringConstants.CONST_COMP_CHARACTERS_REQUIRED)
                .withValidator(new StringLengthValidator(
                        String.format(StringConstants.CONST_COMP_CHARACTERS_VALIDATOR_LENGTH,  attribute.getPrecision()),
                        0, attribute.getPrecision()))
                .bind(CharactersConstData::getStringValue, CharactersConstData::setStringValue);
    }

    /**
     * Defines how and when the user input will be saved.
     * @param binder Binder that manages the input fields.
     */
    private void saveValuesSetup(Binder<CharactersConstData> binder) {
        textField.addValueChangeListener(event -> {
            if (!binder.writeBeanIfValid(optionData)) {
                optionData.setStringValue(null);
            }
        });
    }

    /**
     * Loads previous user input.
     * @param binder Binder that manages the input fields.
     */
    private void loadValuesSetup(Binder<CharactersConstData> binder) {
        if (optionData.getStringValue() != null) {
            binder.readBean(optionData);
        }
    }
}