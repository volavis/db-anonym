package com.volavis.dbanonym.backend.anonymization;

import java.util.List;

/**
 * It contains the new (e.g. generated) value and the table's primary keys. Needed for the batch update.
 */
public class ValueWithPKs {

    private Object value;
    private List<Object> pks;

    /**
     * Constructor.
     * @param pks List of primary keys.
     */
    public ValueWithPKs(List<Object> pks) {
        this.pks = pks;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<Object> getPks() {
        return pks;
    }

    public void setPks(List<Object> pks) {
        this.pks = pks;
    }
}