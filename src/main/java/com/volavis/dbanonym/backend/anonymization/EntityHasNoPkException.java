package com.volavis.dbanonym.backend.anonymization;

/**
 * Exception called if an entity does not have a primary key (update impossible for some options).
 */
public class EntityHasNoPkException extends RuntimeException {

    /**
     * Constructor.
     * @param attributeName Name of the attribute that has no primary key.
     */
    public EntityHasNoPkException(String attributeName) {
        super("Could not anonymize attribute \"" + attributeName + "\", because table has no primary key!");
    }
}