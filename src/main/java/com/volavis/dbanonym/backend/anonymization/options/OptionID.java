package com.volavis.dbanonym.backend.anonymization.options;

import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOption;
import com.volavis.dbanonym.backend.anonymization.options.defoption.DefOption;
import com.volavis.dbanonym.backend.anonymization.options.noneoption.NoneOption;
import com.volavis.dbanonym.backend.anonymization.options.nulloption.NullOption;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOption;

/**
 * Enum for the supported anonymization options.
 */
public enum OptionID {

    NONE("Keine") {
        @Override
        public AnonymizationOption createAnonymizationOption() {
            return new NoneOption();
        }
    },
    DEF("Default") {
        @Override
        public AnonymizationOption createAnonymizationOption() {
            return new DefOption();
        }
    },
    NULL("Null") {
        @Override
        public AnonymizationOption createAnonymizationOption() {
            return new NullOption();
        }
    },
    CONST("Konstante") {
        @Override
        public AnonymizationOption createAnonymizationOption() {
            return new ConstOption();
        }
    },
    RND("Zufall") {
        @Override
        public AnonymizationOption createAnonymizationOption() {
            return new RndOption();
        }
    };

    private final String name;

    /**
     * Returns the correct AnonymizationOption.
     */
    public abstract AnonymizationOption createAnonymizationOption();

    /**
     * Constructor.
     * @param name Readable name of the anonymization option.
     */
    OptionID(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}