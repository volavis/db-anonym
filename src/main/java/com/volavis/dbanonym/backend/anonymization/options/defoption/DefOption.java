package com.volavis.dbanonym.backend.anonymization.options.defoption;

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
 * Anonymization option default: Every value in the column will be replaced by the default value.
 */
public class DefOption extends AnonymizationOption {

    /**
     * Constructor.
     */
    public DefOption() {
        super(OptionID.DEF, false);
    }

    @Override
    public OptionData getOptionData(Attribute attribute) {
        return new DefOptionData();
    }

    @Override
    public OptionComponent getOptionComponent(OptionData optionData, Entity entity, Attribute attribute) {
        return new DefOptionComponent(attribute);
    }

    @Override
    public void update(ConnectedDatabase database, Entity entity, Attribute attribute,
                       List<Attribute> pkList, List<ValueWithPKs> valueWithPKsList) {

        DatabaseVersion databaseVersion = database.getDatabaseVersion();
        JdbcDao dao = database.getDao();
        String dbName = database.getMetadata().getDatabaseName();

        databaseVersion.updateAllRowsWithDefaultValue(dao, dbName, entity, attribute);
    }

    @Override
    public boolean enableRadioButton(Attribute attribute) {
        return attribute.getDefaultValue() != null && !attribute.isUnique();
    }
}