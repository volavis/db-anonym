package com.volavis.dbanonym.backend.database.jdbc;

import com.volavis.dbanonym.backend.anonymization.ValueWithPKs;
import com.volavis.dbanonym.backend.anonymization.additionaldata.listbuilder.DecimalData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.attributes.rdbmsunspecific.UnknownAttribute;
import com.volavis.dbanonym.backend.database.metadata.DbDataType;
import com.volavis.dbanonym.backend.database.metadata.DefaultValue;
import com.volavis.dbanonym.backend.database.metadata.ForeignKey;
import com.volavis.dbanonym.backend.database.metadata.UniqueConstraint;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for database interaction.
 */
@Repository
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Lazy
public class JdbcDao {

    private final ObjectProvider<NamedParameterJdbcTemplate> jtProvider;

    private NamedParameterJdbcTemplate npJdbcTemplate;
    private TransactionTemplate transactionTemplate;

    /**
     * Constructor.
     * @param jtProvider ObjectProvider for NamedParameterJdbcTemplate. Allows parameters to be passed. (autowired)
     */
    @Autowired
    public JdbcDao(@Qualifier("npJdbcTemplate") ObjectProvider<NamedParameterJdbcTemplate> jtProvider) {
        this.jtProvider = jtProvider;
    }

    /**
     * Changes the connected database (the DataSource). Closes the previous connection.
     * Additionally creates the TransactionTemplate.
     * @param settings Settings for the database connection (e.g. user, password, url, etc.)
     */
    public void changeDataSource(ConnectionSettings settings) {
        if(npJdbcTemplate != null) {
            HikariDataSource hikariDataSource = (HikariDataSource) npJdbcTemplate.getJdbcTemplate().getDataSource();
            if(hikariDataSource != null) {
                hikariDataSource.close();     // Without this the connection pools won't get closed!
            }
        }

        npJdbcTemplate = jtProvider.getObject(settings);
        DataSource dataSource = npJdbcTemplate.getJdbcTemplate().getDataSource();
        DataSourceTransactionManager transactionManager;
        if (dataSource != null) {
            transactionManager = new DataSourceTransactionManager(dataSource);
            transactionTemplate = new TransactionTemplate(transactionManager);
        } else {
            throw new IllegalStateException("Datasource null!");
        }
    }

    /**
     * Queries the name of the database.
     * @param sql SQL query.
     * @return Database name.
     */
    public String queryDatabaseName(String sql) {
        if(npJdbcTemplate != null) {
            JdbcTemplate jdbcTemplate = npJdbcTemplate.getJdbcTemplate();
            return jdbcTemplate.queryForObject(sql, String.class);
        } else {
            throw new IllegalStateException("JDBC template null!");
        }
    }

    /**
     * Queries the names of the entities.
     * @param sql SQL query.
     * @return List of entity names.
     */
    public List<String> queryEntityNames(String sql) {
        if(npJdbcTemplate != null) {
            JdbcTemplate jdbcTemplate = npJdbcTemplate.getJdbcTemplate();
            return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getString(1));
        } else {
            throw new IllegalStateException("JDBC template null!");
        }
    }

    /**
     * Queries the names of the entities with their schema.
     * @param sql SQL query.
     * @return List of 'schema_name.entity_name' Strings.
     */
    public List<String> queryEntityNamesWithSchema(String sql) {
        if(npJdbcTemplate != null) {
            JdbcTemplate jdbcTemplate = npJdbcTemplate.getJdbcTemplate();
            List<String> resultList = new ArrayList<>();
            jdbcTemplate.query(sql, resultSet -> {
                String schemaName = resultSet.getString(1);
                String tableName = resultSet.getString(2);
                resultList.add(schemaName + "." + tableName);
            });
            return resultList;
        } else {
            throw new IllegalStateException("JDBC template null!");
        }
    }

    /**
     * Queries an entity's record count (Number of values in the table).
     * @param sql SQL query.
     * @return Record count.
     */
    public Long queryRecordCount(String sql) {
        if(npJdbcTemplate != null) {
            JdbcTemplate jdbcTemplate = npJdbcTemplate.getJdbcTemplate();
            return jdbcTemplate.queryForObject(sql, Long.class);
        } else {
            throw new IllegalStateException("JDBC template null!");
        }
    }

    /**
     * Queries all the attributes of an entity.
     * @param sql SQL query.
     * @return List of attributes.
     */
    public List<Attribute> queryAttributes(String sql) {
        if(npJdbcTemplate != null) {
            JdbcTemplate jdbcTemplate = npJdbcTemplate.getJdbcTemplate();
            return jdbcTemplate.query(sql, resultSet -> {
                List<Attribute> resultList = new ArrayList<>();
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

                for (int column = 1; column <= resultSetMetaData.getColumnCount(); column++) {
                    resultList.add(new UnknownAttribute(resultSetMetaData, column));
                }
                return resultList;
            });
        } else {
            throw new IllegalStateException("JDBC template null!");
        }
    }

    /**
     * Queries the database data types of an entity's attributes.
     * <P>Retrieves the data via plain SQL.
     * @param sql SQL query.
     * @return List of DbType objects (contain the type and the attribute).
     */
    public List<DbDataType> queryDatabaseDataType(String sql) {
        if(npJdbcTemplate != null) {
            JdbcTemplate jdbcTemplate = npJdbcTemplate.getJdbcTemplate();
            List<DbDataType> resultList = new ArrayList<>();
            jdbcTemplate.query(sql, resultSet -> {
                resultList.add(new DbDataType(resultSet.getString(1), resultSet.getString(2)));
            });
            return resultList;
        } else {
            throw new IllegalStateException("JDBC template null!");
        }
    }

    /**
     * Queries the database data types of an entity's attributes.
     * <P>Retrieves the data via the ResultSetMetaData (RSMD).
     * @param sql SQL query.
     * @return List of DbType objects (contain the type and the attribute).
     */
    public List<DbDataType> queryDatabaseDataTypeRSMD(String sql) {
        if(npJdbcTemplate != null) {
            JdbcTemplate jdbcTemplate = npJdbcTemplate.getJdbcTemplate();
            return jdbcTemplate.query(sql, resultSet -> {
                List<DbDataType> resultList = new ArrayList<>();
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

                for (int column = 1; column <= resultSetMetaData.getColumnCount(); column++) {
                    resultList.add(new DbDataType(resultSetMetaData, column));
                }
                return resultList;
            });
        } else {
            throw new IllegalStateException("JDBC template null!");
        }
    }

    /**
     * Queries the default values of an entity's attributes.
     * @param entityName Name of the entity.
     * @return List of DefaultValue objects (contain the default value and the attribute).
     */
    @SuppressWarnings("unchecked")
    public List<DefaultValue> queryDefaultValues(String entityName) throws MetaDataAccessException {
        if(npJdbcTemplate != null) {
            DataSource dataSource = npJdbcTemplate.getJdbcTemplate().getDataSource();
            if (dataSource != null) {
                return (List<DefaultValue>) JdbcUtils.extractDatabaseMetaData(dataSource,
                        databaseMetaData -> getDefaultValues(entityName, databaseMetaData));
            } else {
                throw new IllegalStateException("Datasource null!");
            }
        } else {
            throw new IllegalStateException("JDBC template null!");
        }
    }

    /**
     * Extracts the default values from the DatabaseMetaData.
     * @param entityName Name of the entity.
     * @param databaseMetaData DatabaseMetaData the information will be extracted from.
     */
    private List<DefaultValue> getDefaultValues(String entityName, java.sql.DatabaseMetaData databaseMetaData) throws SQLException {
        ResultSet resultSet = databaseMetaData.getColumns(null, null, entityName, "%");
        List<DefaultValue> resultList = new ArrayList<>();
        while (resultSet.next()) {
            resultList.add(new DefaultValue(
                    resultSet.getString("COLUMN_NAME"),
                    resultSet.getString("COLUMN_DEF")));
        }
        return resultList;
    }

    /**
     * Queries an entity's check constraints (belong to the entity, not to an attribute!).
     * @param sql SQL query.
     * @return List of check constraints.
     */
    public List<String> queryCheckConstraints(String sql) {
        if(npJdbcTemplate != null) {
            JdbcTemplate jdbcTemplate = npJdbcTemplate.getJdbcTemplate();
            return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getString(1));
        } else {
            throw new IllegalStateException("JDBC template null!");
        }
    }

    /**
     * Queries an entity's primary keys.
     * @param entityName Name of the entity.
     * @return List of attribute names.
     */
    @SuppressWarnings("unchecked")
    public List<String> queryPrimaryKeys(String entityName) throws MetaDataAccessException {
        if(npJdbcTemplate != null) {
            DataSource dataSource = npJdbcTemplate.getJdbcTemplate().getDataSource();
            if (dataSource != null) {
                return (List<String>) JdbcUtils.extractDatabaseMetaData(dataSource,
                        databaseMetaData -> getPkAttributes(entityName, databaseMetaData));
            } else {
                throw new IllegalStateException("Datasource null!");
            }
        } else {
            throw new IllegalStateException("JDBC template null!");
        }
    }

    /**
     * Extracts the names of those attributes that are primary keys.
     * @param entityName Name of the entity.
     * @param databaseMetaData DatabaseMetaData the information will be extracted from.
     */
    private List<String> getPkAttributes(String entityName, java.sql.DatabaseMetaData databaseMetaData) throws SQLException {
        ResultSet resultSet = databaseMetaData.getPrimaryKeys(null, null, entityName);
        List<String> resultList = new ArrayList<>();
        while (resultSet.next()) {
            resultList.add(resultSet.getString("COLUMN_NAME"));
        }
        return resultList;
    }

    /**
     * Queries an entity's foreign keys.
     * @param entityName Name of the entity.
     * @return List of ForeignKey objects.
     */
    @SuppressWarnings("unchecked")
    public List<ForeignKey> queryForeignKeys(String entityName) throws MetaDataAccessException {
        if(npJdbcTemplate != null) {
            DataSource dataSource = npJdbcTemplate.getJdbcTemplate().getDataSource();
            if (dataSource != null) {
                return (List<ForeignKey>) JdbcUtils.extractDatabaseMetaData(dataSource,
                        databaseMetaData -> getForeignKeys(entityName, databaseMetaData));
            } else {
                throw new IllegalStateException("Datasource null!");
            }
        } else {
            throw new IllegalStateException("JDBC template null!");
        }
    }

    /**
     * Extracts the entity's foreign keys form the DatabaseMetaData.
     * @param entityName Name of the entity.
     * @param databaseMetaData DatabaseMetaData the information will be extracted from.
     */
    private List<ForeignKey> getForeignKeys(String entityName, java.sql.DatabaseMetaData databaseMetaData) throws SQLException {
        ResultSet resultSet = databaseMetaData.getImportedKeys(null, null, entityName);
        List<ForeignKey> resultList = new ArrayList<>();
        while (resultSet.next()) {
            resultList.add(new ForeignKey(
                    resultSet.getString("FKCOLUMN_NAME"),
                    resultSet.getString("PKTABLE_NAME"),
                    resultSet.getString("PKCOLUMN_NAME")));
        }
        return resultList;
    }

    /**
     * Queries an entity's unique constraints.
     * @param sql SQL query.
     * @return List of UniqueConstraint objects.
     */
    public List<UniqueConstraint> queryUniqueConstraints(String sql) {
        if(npJdbcTemplate != null) {
            JdbcTemplate jdbcTemplate = npJdbcTemplate.getJdbcTemplate();
            List<UniqueConstraint> resultList = new ArrayList<>();
            jdbcTemplate.query(sql, resultSet -> {
                resultList.add(new UniqueConstraint(resultSet.getString(1), resultSet.getString(2)));
            });
            return resultList;
        } else {
            throw new IllegalStateException("JDBC template null!");
        }
    }

    /**
     * Updates all rows with the same value.
     * @param sql SQL query.
     */
    public void updateAllRowsWithSameValue(String sql) {
        if(npJdbcTemplate != null) {
            JdbcTemplate jdbcTemplate = npJdbcTemplate.getJdbcTemplate();
            jdbcTemplate.update(sql);
        } else {
            throw new IllegalStateException("JDBC template null!");
        }
    }

    /**
     * Updates all rows with the same value.
     * @param sql SQL query.
     * @param valueName Name of the named parameter.
     * @param value Value used for updating.
     * @param type Jdbc type of value.
     */
    public void updateAllRowsWithSameValue(String sql, String valueName, Object value, int type) {
        if (npJdbcTemplate != null) {
            SqlParameterSource namedParameters = new MapSqlParameterSource().addValue(valueName, value, type);
            npJdbcTemplate.update(sql, namedParameters);
        } else {
            throw new IllegalStateException("JDBC template null!");
        }
    }

    /**
     * Batch update: Updates all rows with different values.
     * @param sql SQL query.
     * @param batchSize Size of the batch.
     * @param pkAttrList List of primary keys. Used to get the pk types.
     * @param valueType Jdbc type of value.
     * @param valueList List of ValueWithPKs objects. Contain the values and the pk values.
     */
    public void batchUpdateAllRowsDifferentValues(String sql, int batchSize, List<Attribute> pkAttrList, int valueType, List<ValueWithPKs> valueList) {
        transactionTemplate.execute(status -> {
            if(npJdbcTemplate != null) {
                JdbcTemplate jdbcTemplate = npJdbcTemplate.getJdbcTemplate();
                jdbcTemplate.batchUpdate(sql, valueList, batchSize,
                        (preparedStatement, valueWithPKs) -> buildPreparedStatement(pkAttrList, valueType, preparedStatement, valueWithPKs));
            } else {
                throw new IllegalStateException("JDBC template null!");
            }
            return null;
        });
    }

    /**
     * Constructs the prepared statement for the batch update.
     * @param pkAttrList List of primary keys. Used to get the pk types.
     * @param valueType Jdbc type of value.
     * @param preparedStatement PreparedStatement that will be constructed.
     * @param valueWithPKs Contains the update value and the pk values.
     */
    private void buildPreparedStatement(List<Attribute> pkAttrList, int valueType, java.sql.PreparedStatement preparedStatement, ValueWithPKs valueWithPKs) throws SQLException {
        preparedStatement.setObject(1, valueWithPKs.getValue(), valueType);
        List<Object> pkValueList = valueWithPKs.getPks();
        for(int i = 0, pkIndex = 2; i < pkAttrList.size(); i++, pkIndex++) {
            preparedStatement.setObject(pkIndex, pkValueList.get(i), pkAttrList.get(i).getJdbcDataType());
        }
    }

    /**
     * Queries an entity's primary keys.
     * @param sql SQL query.
     * @param aliasPrefix Prefix used for the primary key's named parameters.
     * @param pkCount Number of primary keys.
     * @param valueList List will be updated with the primary keys.
     */
    public void queryEntityPKs(String sql, String aliasPrefix, int pkCount, List<ValueWithPKs> valueList) {
        if(npJdbcTemplate != null) {
            JdbcTemplate jdbcTemplate = npJdbcTemplate.getJdbcTemplate();
            jdbcTemplate.query(sql, resultSet -> {
                List<Object> pkList = new ArrayList<>();
                for(int i = 0; i < pkCount; i++) {
                    pkList.add(resultSet.getObject(aliasPrefix + i));
                }
                valueList.add(new ValueWithPKs(pkList));
            });
        } else {
            throw new IllegalStateException("JDBC template null!");
        }
    }

    /**
     * Queries the metadata length (String = Char length, Byte = Byte length, etc.).
     * @param sql SQL query.
     * @return List of lengths (as Objects).
     */
    public List<Object> queryLengths(String sql) {
        if(npJdbcTemplate != null) {
            JdbcTemplate jdbcTemplate = npJdbcTemplate.getJdbcTemplate();
            List<Object> resultList = new ArrayList<>();
            jdbcTemplate.query(sql, resultSet -> {
                resultList.add(JdbcDaoTools.getInteger(resultSet, 1));
            });
            return resultList;
        } else {
            throw new IllegalStateException("JDBC template null!");
        }
    }

    /**
     * Queries the additional decimal metadata used for the anonymization (length, pre- and post decimal places).
     * @param sql SQL query.
     * @return List of DecimalData (as Objects).
     */
    public List<Object> queryDecimalData(String sql) {
        if(npJdbcTemplate != null) {
            JdbcTemplate jdbcTemplate = npJdbcTemplate.getJdbcTemplate();
            List<Object> resultList = new ArrayList<>();
            jdbcTemplate.query(sql, resultSet -> {
                DecimalData decimalData = new DecimalData();

                Boolean sign = extractSign(resultSet);
                decimalData.setSign(sign);

                Integer preLength = JdbcDaoTools.getInteger(resultSet, 2);
                decimalData.setPreLength(preLength);

                extractAndSetPostDecimalPointLength(resultSet, decimalData, sign, preLength);
                resultList.add(decimalData);
            });
            return resultList;
        } else {
            throw new IllegalStateException("JDBC template null!");
        }
    }

    /**
     * The method extracts the post decimal point length and processes it.
     * <P>The length from the ResultSet is the length of the whole decimal (pre and post length!).
     * It may include a minus sign and the decimal point!
     * @param resultSet ResultSet the post decimal point length gets extracted from.
     * @param decimalData Object that will be updated.
     * @param sign Attribute's sign.
     * @param preLength Attribute's pre decimal point length.
     */
    private void extractAndSetPostDecimalPointLength(ResultSet resultSet, DecimalData decimalData, Boolean sign, Integer preLength) throws SQLException {
        Integer allLength = JdbcDaoTools.getInteger(resultSet, 3);
        if(sign != null && preLength != null && allLength != null) {
            if(!sign) {                                 // If it has a negative sign subtract one
                allLength--;
            }

            if(preLength.equals(allLength)) {           // It has no decimal part
                decimalData.setPostLength(0);
            } else {                                    // Subtract pre decimal part and comma (+1)
                decimalData.setPostLength(allLength - (preLength + 1));
            }
        } else {
            decimalData.setPostLength(null);
        }
    }

    /**
     * Queries the metadata sign (minus, plus, null).
     * @param sql SQL query.
     * @return List of booleans (as Objects).
     */
    public List<Object> querySign(String sql) {
        if(npJdbcTemplate != null) {
            JdbcTemplate jdbcTemplate = npJdbcTemplate.getJdbcTemplate();
            List<Object> resultList = new ArrayList<>();
            jdbcTemplate.query(sql, resultSet -> {
                resultList.add(extractSign(resultSet));
            });
            return resultList;
        } else {
            throw new IllegalStateException("JDBC template null!");
        }
    }

    /**
     * Extracts the attribute's sign.
     * @param resultSet ResultSet the sign gets extracted from.
     * @return Sign as Boolean (true = plus, false = minus, null = null).
     */
    private Boolean extractSign(ResultSet resultSet) throws SQLException {
        Integer signInteger = JdbcDaoTools.getInteger(resultSet, 1);
        if(signInteger == null) {
            return null;
        } else {
            return signInteger >= 0;
        }
    }

    /**
     * Queries all possible ENUM values of the attribute.
     * @param sql SQL query.
     * @return String containing all ENUM values.
     */
    public String queryEnumValues(String sql) {
        if(npJdbcTemplate != null) {
            JdbcTemplate jdbcTemplate = npJdbcTemplate.getJdbcTemplate();
            return jdbcTemplate.queryForObject(sql, String.class);
        } else {
            throw new IllegalStateException("JDBC template null!");
        }
    }

    /**
     * Queries the qualifier. Used for character data types. Is the length in chars or bytes?
     * @param sql SQL query.
     * @return Byte (B); Char (C).
     */
    public String queryQualifierCharacterDataTypes(String sql) {
        if(npJdbcTemplate != null) {
            JdbcTemplate jdbcTemplate = npJdbcTemplate.getJdbcTemplate();
            return jdbcTemplate.queryForObject(sql, String.class);
        } else {
            throw new IllegalStateException("JDBC template null!");
        }
    }

    /**
     * Executes a simple SQL-command.
     * @param sql SQL query.
     */
    public void executeSimpleSQLCommand(String sql) {
        if(npJdbcTemplate != null) {
            JdbcTemplate jdbcTemplate = npJdbcTemplate.getJdbcTemplate();
            jdbcTemplate.execute(sql);
        } else {
            throw new IllegalStateException("JDBC template null!");
        }
    }
}