package com.volavis.dbanonym.backend.anonymization.options.noneoption;

import com.volavis.dbanonym.backend.anonymization.options.OptionID;
import com.volavis.dbanonym.backend.anonymization.options.OptionData;

/**
 * Data class of the anonymization option "none".
 */
public class NoneOptionData extends OptionData {

    public NoneOptionData() {
        super(OptionID.NONE);
    }

    @Override
    public boolean isValid() {
        return true;                // Unnecessary because no user input!
    }
}