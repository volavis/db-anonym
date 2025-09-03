package com.volavis.dbanonym.backend.database.attributes.rdbmsspecific;

import com.volavis.dbanonym.backend.anonymization.additionaldata.AnonymizationDataID;
import com.volavis.dbanonym.backend.anonymization.options.GuiID;
import com.volavis.dbanonym.backend.anonymization.options.constoption.data.MysqlEnumConstData;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndValueGenerator;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.backend.database.JavaDataType;
import com.volavis.dbanonym.backend.database.jdbc.JdbcDao;
import com.volavis.dbanonym.backend.database.rdbms.DatabaseVersion;
import com.volavis.dbanonym.backend.database.rdbms.mysql.MysqlAdditionalMetadata;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Attribute for MySQL's ENUM data type.
 */
public class MysqlEnumAttribute extends Attribute {

    private List<String> enumValues;

    /**
     * Constructor.
     * @param attribute Attribute whose properties will be copied.
     */
    public MysqlEnumAttribute(Attribute attribute) {
        super(attribute, JavaDataType.MYSQL_ENUM, GuiID.MYSQL_ENUM);
    }

    /**
     * Constructor.
     * @param dao JDBC DAO.
     * @param dbVersion The database version.
     * @param dbName Name of the database.
     * @param entity The entity the attribute belongs to.
     * @param attribute Attribute which is currently being considered.
     */
    public MysqlEnumAttribute(JdbcDao dao, DatabaseVersion dbVersion, String dbName, Entity entity, Attribute attribute) {
        this(attribute);
        queryEnumValues(dao, dbVersion, dbName, entity, attribute);
    }

    /**
     * Queries the ENUM values and saves them in the enumValues Set.
     * @param dao JDBC DAO.
     * @param dbVersion The database version.
     * @param dbName Name of the database.
     * @param entity The entity the attribute belongs to.
     * @param attribute Attribute which is currently being considered.
     */
    private void queryEnumValues(JdbcDao dao, DatabaseVersion dbVersion, String dbName, Entity entity, Attribute attribute) {
        MysqlAdditionalMetadata mysqlAdditionalMetadata = (MysqlAdditionalMetadata) dbVersion;
        String enumValues = mysqlAdditionalMetadata.queryEnumValues(dao, dbName, entity, attribute);
        this.enumValues = sortEnumValues(extractEnumValues(enumValues));
    }

    /**
     * Splits the ENUM value string into single values.
     * @param enumValues String containing all ENUM values.
     * @return Set containing the ENUM values.
     */
    private Set<String> extractEnumValues(String enumValues) {
        Set<String> resultSet = new HashSet<>();

        Pattern pattern = Pattern.compile("enum\\((.*)\\)");
        Matcher matcher = pattern.matcher(enumValues);

        if(matcher.matches()) {
            String content = matcher.group(1);

            String[] parts = content.split(",");
            for(String value : parts) {
                resultSet.add(value.substring(1, value.length() - 1));
            }
        }
        return resultSet;
    }

    /**
     * Converts the Set to a List and sorts the List.
     * @param enumSet Set containing the ENUM values.
     * @return List containing the ENUM values.
     */
    List<String> sortEnumValues(Set<String> enumSet) {
        List<String> enumList = new ArrayList<>(enumSet);
        java.util.Collections.sort(enumList);
        return enumList;
    }

    @Override
    public Object getConstValue() {
        MysqlEnumConstData optionData = (MysqlEnumConstData) getAnonymizationOptionData();
        return optionData.getEnumValue();
    }

    @Override
    public Object getRndValue(Map<AnonymizationDataID, Object> additionalDataMap) {
        return RndValueGenerator.randomlyPickEnumValue(enumValues);
    }

    // Getter and setter
    public List<String> getEnumValues() {
        return enumValues;
    }
    public void setEnumValues(List<String> enumValues) {
        this.enumValues = enumValues;
    }
}