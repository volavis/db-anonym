package com.volavis.dbanonym.backend.database.rdbms.mysql;

import com.volavis.dbanonym.backend.database.rdbms.VersionID;

/**
 * MySQL 5.7.
 */
public class Mysql57 extends Mysql56 {

    @Override
    public VersionID getVersionID() {
        return VersionID.MYSQL_57;
    }
}