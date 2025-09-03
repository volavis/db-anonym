package com.volavis.dbanonym.backend.anonymization.options.constoption;

import com.volavis.dbanonym.backend.anonymization.ValueWithPKs;
import com.volavis.dbanonym.backend.anonymization.options.AnonymizationOption;
import com.volavis.dbanonym.backend.anonymization.options.OptionComponent;
import com.volavis.dbanonym.backend.anonymization.options.OptionData;
import com.volavis.dbanonym.backend.anonymization.options.OptionID;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.ConnectedDatabase;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.backend.database.JavaDataType;
import com.volavis.dbanonym.backend.database.jdbc.JdbcDao;
import com.volavis.dbanonym.backend.database.rdbms.DatabaseVersion;

import java.util.List;

/**
 * Anonymization option constant: Every value in the column will be replaced by a constant value.
 */
public class ConstOption extends AnonymizationOption {

    /**
     * Constructor.
     */
    public ConstOption() {
        super(OptionID.CONST, false);
    }

    @Override
    public OptionData getOptionData(Attribute attribute) {
        return attribute.getGuiID().getConstOptionData();
    }

    @Override
    public OptionComponent getOptionComponent(OptionData optionData, Entity entity, Attribute attribute) {
        return attribute.getGuiID().getConstOptionComponent((ConstOptionData) optionData, entity, attribute);
    }

    @Override
    public void update(ConnectedDatabase database, Entity entity, Attribute attribute,
                       List<Attribute> pkList, List<ValueWithPKs> valueWithPKsList) {

        DatabaseVersion databaseVersion = database.getDatabaseVersion();
        JdbcDao dao = database.getDao();
        String dbName = database.getMetadata().getDatabaseName();

        Object tmpObject = attribute.getConstValue();
        databaseVersion.updateAllRowsWithSameValue(dao, dbName, entity, attribute, tmpObject);
    }

    @Override
    public boolean enableRadioButton(Attribute attribute) {
        return !attribute.isUnique() && attribute.getJavaDataType() != JavaDataType.UNKNOWN;
    }
}