package com.volavis.dbanonym.backend.anonymization.options.constoption.components;

import com.vaadin.flow.component.html.Label;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionComponent;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;

/**
 * Builds the (const) GUI for the GuiID Unknown.
 */
public class UnknownConstGui extends ConstOptionComponent {

    /**
     * Constructor.
     * @param entity Entity the attribute belongs to.
     * @param attribute The GUI will be build based on the attribute's properties.
     */
    public UnknownConstGui(Entity entity, Attribute attribute) {
        super(entity, attribute);

        buildComponentLayout();
        binderSetup();
    }

    @Override
    protected void buildComponentLayout() {
        add(new Label(StringConstants.CONST_COMP_UNKNOWN_TITLE));
    }

    @Override
    protected void binderSetup() {
        // Not needed!
    }
}