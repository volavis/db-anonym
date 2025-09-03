package com.volavis.dbanonym.backend.anonymization.options.nulloption;

import com.volavis.dbanonym.backend.anonymization.options.OptionID;
import com.volavis.dbanonym.backend.anonymization.options.OptionData;

/**
 * Data class of the anonymization option "null".
 */
public class NullOptionData extends OptionData {

    public NullOptionData() {
        super(OptionID.NULL);
    }

    @Override
    public boolean isValid() {
        return true;                // Unnecessary because no user input!
    }
}