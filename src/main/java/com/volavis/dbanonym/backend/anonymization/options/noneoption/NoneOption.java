package com.volavis.dbanonym.backend.anonymization.options.noneoption;

import com.volavis.dbanonym.backend.anonymization.ValueWithPKs;
import com.volavis.dbanonym.backend.anonymization.options.AnonymizationOption;
import com.volavis.dbanonym.backend.anonymization.options.OptionComponent;
import com.volavis.dbanonym.backend.anonymization.options.OptionData;
import com.volavis.dbanonym.backend.anonymization.options.OptionID;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.ConnectedDatabase;
import com.volavis.dbanonym.backend.database.Entity;

import java.util.List;

/**
 * Anonymization option none: Attribute will not be anonymized.
 */
public class NoneOption extends AnonymizationOption {

    /**
     * Constructor.
     */
    public NoneOption() {
        super(OptionID.NONE, false);
    }

    @Override
    public OptionData getOptionData(Attribute attribute) {
        return new NoneOptionData();
    }

    @Override
    public OptionComponent getOptionComponent(OptionData optionData, Entity entity, Attribute attribute) {
        return new NoneOptionComponent();
    }

    @Override
    public void update(ConnectedDatabase database, Entity entity, Attribute attribute,
                       List<Attribute> pkList, List<ValueWithPKs> valueWithPKsList) {
        // Not needed!
    }

    @Override
    public boolean enableRadioButton(Attribute attribute) {
        return true;    // Option never disabled!
    }
}