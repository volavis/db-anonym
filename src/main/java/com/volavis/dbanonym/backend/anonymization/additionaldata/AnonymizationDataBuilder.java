package com.volavis.dbanonym.backend.anonymization.additionaldata;

import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.ConnectedDatabase;
import com.volavis.dbanonym.backend.database.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Queries the additional data needed for the anonymization.
 */
public class AnonymizationDataBuilder {

    /**
     * This method queries the additional data needed for the anonymization.
     * @param database Database the user connected to.
     * @param entity Entity the attribute belongs to.
     * @param attribute Attribute that will be anonymized.
     * @param recordCount Number of records in the table.
     * @param dataIDList Contains which metadata must be retrieved from the database depending on the GuiID.
     * @return List containing Maps for each table record. The Map stores the additional data as an Object and uses the enum as key.
     */
    public List<Map<AnonymizationDataID, Object>> queryAdditionalAnonymizationData(
            ConnectedDatabase database, Entity entity, Attribute attribute, long recordCount, List<AnonymizationDataID> dataIDList) {

        // Init
        List<Map<AnonymizationDataID, Object>> resultList = new ArrayList<>();
        for(int row = 0; row < recordCount; row++) {
            resultList.add(new HashMap<>());
        }

        // Query metadata
        for(AnonymizationDataID dataID : dataIDList) {
            AnonymizationDataListBuilder listBuilder = dataID.getListBuilder();
            listBuilder.queryObjectList(database, entity, attribute);
            for (int row = 0; row < recordCount; row++) {
                resultList.get(row).put(dataID, listBuilder.getValue(row));
            }
        }

        return resultList;
    }
}