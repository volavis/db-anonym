package com.volavis.dbanonym.backend.database.metadata;

import com.volavis.dbanonym.backend.database.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains the database's metadata, e.g. database name, entities with attributes.
 */
public class Metadata {

    private String databaseName;
    private final Map<String, Entity> entities = new HashMap<>();

    public String getDatabaseName() {
        return databaseName;
    }
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
    public Map<String, Entity> getEntities() {
        return entities;
    }
    public void addEntity(Entity entity) {
        this.entities.put(entity.getName(), entity);
    }
}