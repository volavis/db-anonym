package com.volavis.dbanonym.backend.anonymization.additionaldata;

import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.ConnectedDatabase;
import com.volavis.dbanonym.backend.database.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Superclass for the additional anonymization data list builders.
 * <P>They implement how the metadata will be queried and keep the metadata in an Object-List.
 */
public abstract class AnonymizationDataListBuilder {

    private List<Object> objectList = new ArrayList<>();

    /**
     * Queries an Object-List containing the additional metadata used in the anonymization.
     * The Object-List is supposed to be assigned to the objectList attribute!
     * @param database Database the user connected to.
     * @param entity Entity the attribute belongs to.
     * @param attribute Attribute that will be anonymized. Metadata will be retrieved for this attribute.
     */
    public abstract void queryObjectList(ConnectedDatabase database, Entity entity, Attribute attribute);

    /**
     * Returns the Object from the objectList attribute at the passed index.
     * @param index Index in the Object-List.
     * @return Object at index.
     */
    public Object getValue(int index) {
        if(index >= objectList.size()) {
            throw new IllegalArgumentException("Index does not exist!");
        } else {
            return objectList.get(index);
        }
    }

    // Setter
    public void setList(List<Object> objectList) {
        this.objectList = objectList;
    }
}