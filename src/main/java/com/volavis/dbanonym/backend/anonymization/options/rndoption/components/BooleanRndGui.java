package com.volavis.dbanonym.backend.anonymization.options.rndoption.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionComponent;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionData;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.data.BooleanRndData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;

/**
 * Builds the (rnd) GUI for the GuiID Boolean.
 */
public class BooleanRndGui extends RndOptionComponent {

    private final BooleanRndData optionData;

    /**
     * Constructor.
     * @param optionData Data class that saves the user input.
     * @param entity Entity the attribute belongs to.
     * @param attribute The GUI will be build based on the attribute's properties.
     */
    public BooleanRndGui(RndOptionData optionData, Entity entity, Attribute attribute) {
        super(entity, attribute);
        this.optionData = (BooleanRndData) optionData;

        buildComponentLayout();
        binderSetup();
    }

    @Override
    protected void buildComponentLayout() {
        // Warning when boolean attribute is unique. Makes no sense to use random!
        if(attribute.isUnique()) {
            HorizontalLayout warningLayout = new HorizontalLayout();
            warningLayout.setId("boolean-warning");
            Span warningText = new Span(StringConstants.RND_COMP_BOOLEAN_UNIQUE);
            warningLayout.add(new Icon(VaadinIcon.WARNING), warningText);
            add(warningLayout);
            optionData.setAttributeUnique(true);
        } else {
            add(new Span(StringConstants.RND_COMP_BOOLEAN_TITLE));
            optionData.setAttributeUnique(false);
        }
    }

    @Override
    protected void binderSetup() {
        // Not needed!
    }
}