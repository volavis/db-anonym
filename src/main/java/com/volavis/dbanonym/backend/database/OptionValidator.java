package com.volavis.dbanonym.backend.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Responsible for validating the anonymization options.
 */
public class OptionValidator {

    /**
     * Checks if the anonymization option of every attribute is valid.
     * @param entityMap Map containing the entities.
     * @return List of invalid (simple)attributes.
     */
    public List<SimpleAttribute> validate(Map<String, Entity> entityMap) {
        List<SimpleAttribute> simpleAttributeArrayList = new ArrayList<>();
        for (Entity entity : entityMap.values()) {
            for (Attribute attribute : entity.getAttributes().values()) {
                if (attribute.getAnonymizationOptionData() != null) {
                    if (!attribute.isValid()) {
                        simpleAttributeArrayList.add(new SimpleAttribute(entity.getName(), attribute.getName(),
                                attribute.getAnonymizationOptionData().getOptionID(), null));
                    }
                }
            }
        }
        return simpleAttributeArrayList;
    }
}