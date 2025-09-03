package com.volavis.dbanonym.backend.anonymization.options.rndoption.data;

import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionData;

/**
 * Saves the (rnd) GUI user input for the GuiID Boolean.
 */
public class BooleanRndData extends RndOptionData {

    private boolean attributeUnique;     // Cannot anonymize when unique!

    @Override
    public boolean isValid() {
        return !attributeUnique;
    }

    // Getter and setter
    public boolean isAttributeUnique() {
        return attributeUnique;
    }
    public void setAttributeUnique(boolean attributeUnique) {
        this.attributeUnique = attributeUnique;
    }
}