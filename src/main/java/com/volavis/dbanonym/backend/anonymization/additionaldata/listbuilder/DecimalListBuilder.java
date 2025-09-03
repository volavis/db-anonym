package com.volavis.dbanonym.backend.anonymization.additionaldata.listbuilder;

import com.volavis.dbanonym.backend.anonymization.additionaldata.AnonymizationDataListBuilder;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.ConnectedDatabase;
import com.volavis.dbanonym.backend.database.Entity;

import java.util.List;

/**
 * Queries the decimal data for all the attribute's values.
 * <P>Consists out of the sign and the pre/post decimal point length.
 */
public class DecimalListBuilder extends AnonymizationDataListBuilder {

    @Override
    public void queryObjectList(ConnectedDatabase database, Entity entity, Attribute attribute) {
        List<Object> list = database.getDatabaseVersion()
                .queryDecimalData(database.getDao(), database.getMetadata().getDatabaseName(), entity, attribute);
        setList(list);
    }
}