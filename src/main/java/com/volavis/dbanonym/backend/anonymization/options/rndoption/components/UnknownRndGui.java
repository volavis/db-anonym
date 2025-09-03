package com.volavis.dbanonym.backend.anonymization.options.rndoption.components;

import com.vaadin.flow.component.html.Label;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionComponent;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;

/**
 * Builds the (rnd) GUI for the GuiID Unknown.
 */
public class UnknownRndGui extends RndOptionComponent {

    /**
     * Constructor.
     * @param entity Entity the attribute belongs to.
     * @param attribute The GUI will be build based on the attribute's properties.
     */
    public UnknownRndGui(Entity entity, Attribute attribute) {
        super(entity, attribute);

        buildComponentLayout();
        binderSetup();
    }

    @Override
    protected void buildComponentLayout() {
        add(new Label(StringConstants.RND_COMP_UNKNOWN_TITLE));
    }

    @Override
    protected void binderSetup() {
        // Not needed!
    }
}