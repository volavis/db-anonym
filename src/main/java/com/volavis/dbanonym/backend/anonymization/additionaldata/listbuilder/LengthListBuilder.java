package com.volavis.dbanonym.backend.anonymization.additionaldata.listbuilder;

import com.volavis.dbanonym.backend.anonymization.additionaldata.AnonymizationDataListBuilder;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.ConnectedDatabase;
import com.volavis.dbanonym.backend.database.Entity;

import java.util.List;

/**
 * Queries the length (size) for all the attribute's values.
 * <P>E.g. character length, digit count, byte size, etc.
 */
public class LengthListBuilder extends AnonymizationDataListBuilder {

    @Override
    public void queryObjectList(ConnectedDatabase database, Entity entity, Attribute attribute) {
        List<Object> list = database.getDatabaseVersion()
                .queryLengths(database.getDao(), database.getMetadata().getDatabaseName(), entity, attribute);
        setList(list);
    }
}