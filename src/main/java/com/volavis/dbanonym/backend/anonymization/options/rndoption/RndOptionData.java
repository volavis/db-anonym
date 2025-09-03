package com.volavis.dbanonym.backend.anonymization.options.rndoption;

import com.volavis.dbanonym.backend.anonymization.options.OptionData;
import com.volavis.dbanonym.backend.anonymization.options.OptionID;

/**
 * Superclass for the data classes of the anonymization option "random".
 */
public abstract class RndOptionData extends OptionData {

    private boolean showInitialBounds = true;

    public RndOptionData() {
        super(OptionID.RND);
    }

    // Getter and setter
    public boolean showInitialBounds() {
        return showInitialBounds;
    }
    public void setShowInitialBounds(boolean showInitialBounds) {
        this.showInitialBounds = showInitialBounds;
    }
}