package com.volavis.dbanonym.backend.database.jdbc;

import com.volavis.dbanonym.backend.database.rdbms.RdbmsID;
import com.volavis.dbanonym.backend.database.rdbms.VersionID;

/**
 * Contains the settings needed for the JDBC database connection.
 */
public class ConnectionSettings {

    private RdbmsID rdbms;
    private VersionID version;
    private String user;
    private String password;
    private String url;

    public RdbmsID getRdbms() {
        return rdbms;
    }

    public void setRdbms(RdbmsID rdbms) {
        this.rdbms = rdbms;
    }

    public VersionID getVersion() {
        return version;
    }

    public void setVersion(VersionID version) {
        this.version = version;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}