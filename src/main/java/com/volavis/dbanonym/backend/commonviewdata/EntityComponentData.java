package com.volavis.dbanonym.backend.commonviewdata;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import java.util.ArrayList;
import java.util.List;

/**
 * Saves which EntityComponents are open and the current EntityComponent. Used in TablesView.
 */
@SpringComponent
@VaadinSessionScope
public class EntityComponentData {

    private String currentEntityComponentId = "";
    private List<String> openedEntityComponentsList = new ArrayList<>();

    // Getter and setter
    public String getCurrentEntityComponentId() {
        return currentEntityComponentId;
    }

    public void setCurrentEntityComponentId(String currentEntityComponentId) {
        this.currentEntityComponentId = currentEntityComponentId;
    }

    public List<String> getOpenedEntityComponentsList() {
        return openedEntityComponentsList;
    }

    public void setOpenedEntityComponentsList(List<String> openedEntityComponentsList) {
        this.openedEntityComponentsList = openedEntityComponentsList;
    }
}