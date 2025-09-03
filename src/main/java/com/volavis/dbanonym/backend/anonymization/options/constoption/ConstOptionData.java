package com.volavis.dbanonym.backend.anonymization.options.constoption;

import com.volavis.dbanonym.backend.anonymization.options.OptionData;
import com.volavis.dbanonym.backend.anonymization.options.OptionID;

/**
 * Superclass for the data classes of the anonymization option "constant".
 */
public abstract class ConstOptionData extends OptionData {

    public ConstOptionData() {
        super(OptionID.CONST);
    }
}