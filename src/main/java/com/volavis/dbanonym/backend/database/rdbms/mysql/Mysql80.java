package com.volavis.dbanonym.backend.database.rdbms.mysql;

import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.backend.database.rdbms.VersionID;
import com.volavis.dbanonym.backend.database.jdbc.JdbcDao;

import java.util.List;

/**
 * MySQL 8.0.
 */
public class Mysql80 extends Mysql57 {

    @Override
    public VersionID getVersionID() {
        return VersionID.MYSQL_80;
    }

    @Override
    public List<String> queryCheckConstraints(JdbcDao jdbcDao, String dbName, Entity entity) {
        String sql = "SELECT C.CHECK_CLAUSE FROM INFORMATION_SCHEMA.CHECK_CONSTRAINTS AS C " +
                "JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS T ON C.CONSTRAINT_NAME = T.CONSTRAINT_NAME " +
                "WHERE C.CONSTRAINT_SCHEMA = '" + dbName + "' AND T.TABLE_NAME = '" + entity.getName() + "';";
        return jdbcDao.queryCheckConstraints(sql);
    }
}