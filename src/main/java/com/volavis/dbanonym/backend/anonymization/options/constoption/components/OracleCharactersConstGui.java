package com.volavis.dbanonym.backend.anonymization.options.constoption.components;

import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionData;
import com.volavis.dbanonym.backend.anonymization.options.constoption.data.CharactersConstData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.backend.database.attributes.rdbmsspecific.OracleStringAttribute;

import java.nio.charset.StandardCharsets;

/**
 * Builds the (const) GUI for the GuiID Oracle_Characters.
 */
public class OracleCharactersConstGui extends CharactersConstGui {

    /**
     * Constructor.
     * @param optionData Data class that saves the user input.
     * @param entity     Entity the attribute belongs to.
     * @param attribute  The GUI will be build based on the attribute's properties.
     */
    public OracleCharactersConstGui(ConstOptionData optionData, Entity entity, Attribute attribute) {
        super(optionData, entity, attribute);
    }

    /**
     * Binds input fields.
     * @param binder Binder the fields will be bound to.
     */
    @Override
    protected void bindFields(Binder<CharactersConstData> binder) {
        OracleStringAttribute oracleStringAttribute = (OracleStringAttribute) attribute;

        Binder.BindingBuilder<CharactersConstData, String> builder = binder.forField(textField);

        builder.asRequired(StringConstants.CONST_COMP_CHARACTERS_REQUIRED);
        if(oracleStringAttribute.usesBytes()) {
            builder.withValidator(text -> text.getBytes(StandardCharsets.UTF_8).length <= attribute.getPrecision(),
                    String.format(StringConstants.CONST_COMP_ORACLE_CHARACTERS_VALIDATOR_LENGTH,  attribute.getPrecision()));
        } else {
            builder.withValidator(new StringLengthValidator(
                    String.format(StringConstants.CONST_COMP_CHARACTERS_VALIDATOR_LENGTH,  attribute.getPrecision()),
                    0, attribute.getPrecision()));
        }
        builder.bind(CharactersConstData::getStringValue, CharactersConstData::setStringValue);
    }
}