package com.volavis.dbanonym.backend.commonviewdata;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;

/**
 * Saves the attribute (and its entity) for which the user wants to choose an anonymization option.
 */
@SpringComponent
@VaadinSessionScope
public class SelectedAttribute {

    private Attribute attribute;
    private Entity entity;

    /**
     * Updates all values.
     * @param attribute The attribute which is currently being considered.
     * @param entity The attribute's entity.
     */
    public void update(Attribute attribute, Entity entity) {
        this.attribute = attribute;
        this.entity = entity;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public Entity getEntity() {
        return entity;
    }
}