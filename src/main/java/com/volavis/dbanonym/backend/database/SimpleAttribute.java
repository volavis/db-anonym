package com.volavis.dbanonym.backend.database;

import com.volavis.dbanonym.backend.anonymization.options.OptionID;

import java.util.Objects;

/**
 * Slimmed down attribute that additionally stores the entity name and an exception.
 */
public class SimpleAttribute {

    private final String entityName;
    private final String attributeName;
    private final OptionID option;
    private final Throwable cause;

    /**
     * Constructor.
     * @param entityName Name of the attribute's entity.
     * @param attributeName Name of the attribute.
     * @param option Anonymization option of the attribute.
     * @param cause Exception that was caught while trying to anonymize the attribute.
     */
    public SimpleAttribute(String entityName, String attributeName, OptionID option, Throwable cause) {
        this.entityName = entityName;
        this.attributeName = attributeName;
        this.option = option;
        this.cause = cause;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleAttribute that = (SimpleAttribute) o;
        return Objects.equals(getEntityName(), that.getEntityName()) && Objects.equals(getAttributeName(), that.getAttributeName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEntityName(), getAttributeName());
    }

    public String getEntityName() {
        return entityName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public OptionID getOption() {
        return option;
    }

    public Throwable getCause() {
        return cause;
    }
}