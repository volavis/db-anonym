package com.volavis.dbanonym.backend.anonymization.options.rndoption.components;

import com.vaadin.flow.component.html.Label;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionComponent;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionData;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.data.DecimalRndData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;

/**
 * Builds the (rnd) GUI for the GuiID Oracle_Decimal.
 */
public class OracleDecimalRndGui extends RndOptionComponent {

    protected final DecimalRndData optionData;

    /**
     * Constructor.
     * @param optionData Data class that saves the user input.
     * @param entity     Entity the attribute belongs to.
     * @param attribute  The GUI will be build based on the attribute's properties.
     */
    public OracleDecimalRndGui(RndOptionData optionData, Entity entity, Attribute attribute) {
        super(entity, attribute);
        this.optionData = (DecimalRndData) optionData;

        buildComponentLayout();
        binderSetup();
    }

    @Override
    protected void buildComponentLayout() {
        add(new Label(StringConstants.RND_COMP_ORACLE_DECIMAL));
    }

    @Override
    protected void binderSetup() {
        // Not yet implemented!
    }
}