package com.volavis.dbanonym.backend.anonymization.options.defoption;

import com.volavis.dbanonym.backend.anonymization.options.OptionID;
import com.volavis.dbanonym.backend.anonymization.options.OptionData;

/**
 * Data class of the anonymization option "default".
 */
public class DefOptionData extends OptionData {

    public DefOptionData() {
        super(OptionID.DEF);
    }

    @Override
    public boolean isValid() {
        return true;                // Unnecessary because no user input!
    }
}