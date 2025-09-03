package com.volavis.dbanonym.backend.anonymization.options.constoption.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionComponent;
import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionData;
import com.volavis.dbanonym.backend.anonymization.options.constoption.data.BooleanConstData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;

/**
 * Builds the (const) GUI for the GuiID Boolean.
 */
public class BooleanConstGui extends ConstOptionComponent {

    private final BooleanConstData optionData;

    /**
     * Constructor.
     * @param optionData Data class that saves the user input.
     * @param entity Entity the attribute belongs to.
     * @param attribute The GUI will be build based on the attribute's properties.
     */
    public BooleanConstGui(ConstOptionData optionData, Entity entity, Attribute attribute) {
        super(entity, attribute);
        this.optionData = (BooleanConstData) optionData;

        buildComponentLayout();
        binderSetup();
    }

    @Override
    protected void buildComponentLayout() {
        Span title = new Span(StringConstants.CONST_COMP_BOOLEAN_TITLE);

        RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
        radioGroup.setItems("true", "false");
        radioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        radioGroup.addValueChangeListener(event -> optionData.setBooleanValue(Boolean.parseBoolean(event.getValue())));

        Boolean booleanValue = optionData.getBooleanValue();
        if (booleanValue != null) {
            radioGroup.setValue(booleanValue.toString());
        } else {
            radioGroup.setValue("true");
        }

        add(title, radioGroup);
    }

    @Override
    protected void binderSetup() {
        // Not needed!
    }
}