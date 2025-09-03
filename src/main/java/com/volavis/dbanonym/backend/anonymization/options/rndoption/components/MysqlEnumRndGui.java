package com.volavis.dbanonym.backend.anonymization.options.rndoption.components;

import com.vaadin.flow.component.html.Span;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionComponent;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;

/**
 * Builds the (rnd) GUI for the GuiID MYSQL_ENUM.
 */
public class MysqlEnumRndGui extends RndOptionComponent {

    /**
     * Constructor.
     * @param entity Entity the attribute belongs to.
     * @param attribute The GUI will be build based on the attribute's properties.
     */
    public MysqlEnumRndGui(Entity entity, Attribute attribute) {
        super(entity, attribute);

        buildComponentLayout();
        binderSetup();
    }

    @Override
    protected void buildComponentLayout() {
        add(new Span(StringConstants.RND_COMP_MYSQL_ENUM_TITLE));
    }

    @Override
    protected void binderSetup() {
        // Not needed!
    }
}