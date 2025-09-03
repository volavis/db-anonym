package com.volavis.dbanonym.backend.anonymization.additionaldata;

import com.volavis.dbanonym.backend.anonymization.additionaldata.listbuilder.DecimalListBuilder;
import com.volavis.dbanonym.backend.anonymization.additionaldata.listbuilder.SignListBuilder;
import com.volavis.dbanonym.backend.anonymization.additionaldata.listbuilder.LengthListBuilder;

/**
 * Enum for the AnonymizationDataIDs.
 * <P>Some AnonymizationOptions need additional metadata for the anonymization.
 * <P>E.g. RND needs metadata (char count, sign, decimal places, etc.) to create the random values.
 */
public enum AnonymizationDataID {
    LENGTH {
        @Override
        public AnonymizationDataListBuilder getListBuilder() {
            return new LengthListBuilder();
        }
    },
    SIGN {
        @Override
        public AnonymizationDataListBuilder getListBuilder() {
            return new SignListBuilder();
        }
    },
    DECIMAL {
        @Override
        public AnonymizationDataListBuilder getListBuilder() {
            return new DecimalListBuilder();
        }
    };

    /**
     * Returns the correct AnonymizationDataListBuilder.
     */
    public abstract AnonymizationDataListBuilder getListBuilder();
}