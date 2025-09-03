package com.volavis.dbanonym.backend.anonymization.options.rndoption;

import com.volavis.dbanonym.backend.anonymization.ValueWithPKs;
import com.volavis.dbanonym.backend.anonymization.additionaldata.AnonymizationDataBuilder;
import com.volavis.dbanonym.backend.anonymization.additionaldata.AnonymizationDataID;
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

import java.util.*;

/**
 * Anonymization option random: Every value in the column will be replaced by a random value.
 */
public class RndOption extends AnonymizationOption {

    /**
     * Constructor.
     */
    public RndOption() {
        super(OptionID.RND, true);
    }

    @Override
    public OptionData getOptionData(Attribute attribute) {
        return attribute.getGuiID().getRndOptionData();
    }

    @Override
    public OptionComponent getOptionComponent(OptionData optionData, Entity entity, Attribute attribute) {
        return attribute.getGuiID().getRndOptionComponent((RndOptionData) optionData, entity, attribute);
    }

    @Override
    public void update(ConnectedDatabase database, Entity entity, Attribute attribute,
                       List<Attribute> pkList, List<ValueWithPKs> valueWithPKsList) {

        DatabaseVersion databaseVersion = database.getDatabaseVersion();
        JdbcDao dao = database.getDao();
        String dbName = database.getMetadata().getDatabaseName();

        List<Map<AnonymizationDataID, Object>> additionalDataList = null;
        List<AnonymizationDataID> dataIDList = attribute.getGuiID().getRndAnonymizationDataIDList(attribute);
        if(dataIDList != null && !dataIDList.isEmpty()) {
            AnonymizationDataBuilder builder = new AnonymizationDataBuilder();
            additionalDataList = builder.queryAdditionalAnonymizationData(database, entity, attribute, valueWithPKsList.size(), dataIDList);
        }

        if(attribute.isUnique()) {

            // TODO: Add shuffle variant or timeout
            Set<Object> objectSet = new LinkedHashSet <>();
            while(objectSet.size() < valueWithPKsList.size()) {
                objectSet.add(attribute.getRndValue(
                        additionalDataList != null ? additionalDataList.get(objectSet.size()) : null));
            }

            // Integrate set into list
            int i = 0;
            for (Iterator<Object> it = objectSet.iterator(); it.hasNext(); i++) {
                valueWithPKsList.get(i).setValue(it.next());
            }

        } else {

            for(int i = 0; i < valueWithPKsList.size(); i++) {
                valueWithPKsList.get(i).setValue(attribute.getRndValue(
                        additionalDataList != null ? additionalDataList.get(i) : null));
            }
        }

        // TODO Remove (Intentional: Application slows down)
        /*
        Set<Object> test = new HashSet<>();
        int valueCount = 1000000;
        while(test.size() < valueCount) {
            test.add(ThreadLocalRandom.current().nextInt(0, valueCount + 1));
        }
        */

        if(attribute.isUnique()) {
            databaseVersion.dropUniqueConstraint(dao, dbName, entity, attribute);
            try {
                databaseVersion.batchUpdateAllRowsDifferentValues(dao, dbName, 500, entity, attribute, pkList, valueWithPKsList);
            } finally {
                databaseVersion.createUniqueConstraint(dao, dbName, entity, attribute);
            }

        } else {
            databaseVersion.batchUpdateAllRowsDifferentValues(dao, dbName, 500, entity, attribute, pkList, valueWithPKsList);
        }
    }

    @Override
    public boolean enableRadioButton(Attribute attribute) {
        return attribute.getJavaDataType() != JavaDataType.UNKNOWN;
    }
}