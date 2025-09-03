package com.volavis.dbanonym.backend.commonviewdata;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

/**
 * Saves the state of the database tab in the MainView drawer.
 */
@SpringComponent
@VaadinSessionScope
public class MenuDatabaseTab {

    private boolean isConnected = false;
    private String dbName = "";

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}