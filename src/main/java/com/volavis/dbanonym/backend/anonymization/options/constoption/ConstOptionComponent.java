package com.volavis.dbanonym.backend.anonymization.options.constoption;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.OptionComponent;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;

/**
 * Superclass for the GUIs of the anonymization option "constant".
 */
@CssImport("./styles/components/anonymizationoptions/const-option-component.css")
public abstract class ConstOptionComponent extends OptionComponent {

    protected final Entity entity;
    protected final Attribute attribute;

    /**
     * Constructor.
     * @param entity Entity the attribute belongs to.
     * @param attribute The GUI will be build based on the attribute's properties.
     */
    public ConstOptionComponent(Entity entity, Attribute attribute) {
        super();
        this.entity = entity;
        this.attribute = attribute;

        add(buildOptionInfoLayout(StringConstants.CONST_COMP_OPTION_INFO));
        VerticalLayout checkConstraintsLayout = buildCheckConstraintsLayout(attribute);
        if(checkConstraintsLayout != null) {
            add(checkConstraintsLayout);
        }
    }

    /**
     * Builds the layout of the component.
     */
    protected abstract void buildComponentLayout();

    /**
     * Contains everything that has to do with binders.
     */
    protected abstract void binderSetup();
}