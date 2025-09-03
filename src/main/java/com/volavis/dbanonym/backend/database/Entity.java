package com.volavis.dbanonym.backend.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Database entity (table).
 */
public class Entity {

    private final String schema;    // Depends on RDBMS: E.g. MySQL does not use schemas!
    private final String name;
    private final Map<String, Attribute> attributes = new HashMap<>();
    private final List<String> checkConstraints = new ArrayList<>();
    private Long recordCount;

    /**
     * Constructor.
     * @param schema Name of the entity's schema (Null if the RDBMS does not use schemas).
     * @param name Name of the entity.
     */
    public Entity(String schema, String name) {
        this.schema = schema;
        this.name = name;
    }

    /**
     * Adds a list of attributes to the attributes map.
     * @param attributeList List of attributes that will be added.
     */
    public void addAttributeList(List<Attribute> attributeList) {
        for(Attribute attribute : attributeList) {
            this.attributes.put(attribute.getName(), attribute);
        }
    }

    /**
     * Adds a check constraint to the checkConstraints list.
     * @param constraint Check constraint that will be added.
     */
    public void addCheckConstraint(String constraint) {
        this.checkConstraints.add(constraint);
    }

    public String getName() {
        return name;
    }

    public String getSchema() {
        return schema;
    }

    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    public List<String> getCheckConstraints() {
        return checkConstraints;
    }

    public Long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Long recordCount) {
        this.recordCount = recordCount;
    }
}