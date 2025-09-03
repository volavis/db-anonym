package com.volavis.dbanonym.backend.anonymization.options.nulloption;

import com.volavis.dbanonym.backend.anonymization.ValueWithPKs;
import com.volavis.dbanonym.backend.anonymization.options.AnonymizationOption;
import com.volavis.dbanonym.backend.anonymization.options.OptionComponent;
import com.volavis.dbanonym.backend.anonymization.options.OptionData;
import com.volavis.dbanonym.backend.anonymization.options.OptionID;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.ConnectedDatabase;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.backend.database.jdbc.JdbcDao;
import com.volavis.dbanonym.backend.database.rdbms.DatabaseVersion;

import java.util.List;

/**
 * Anonymization option null: Every value in the column will be replaced by null.
 */
public class NullOption extends AnonymizationOption {

    /**
     * Constructor.
     */
    public NullOption() {
        super(OptionID.NULL, false);
    }

    @Override
    public OptionData getOptionData(Attribute attribute) {
        return new NullOptionData();
    }

    @Override
    public OptionComponent getOptionComponent(OptionData optionData, Entity entity, Attribute attribute) {
        return new NullOptionComponent();
    }

    @Override
    public void update(ConnectedDatabase database, Entity entity, Attribute attribute,
                       List<Attribute> pkList, List<ValueWithPKs> valueWithPKsList) {

        DatabaseVersion databaseVersion = database.getDatabaseVersion();
        JdbcDao dao = database.getDao();
        String dbName = database.getMetadata().getDatabaseName();

        databaseVersion.updateAllRowsWithNull(dao, dbName, entity, attribute);
    }

    @Override
    public boolean enableRadioButton(Attribute attribute) {
        return attribute.isNullable();
    }
}