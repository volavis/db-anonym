package com.volavis.dbanonym.backend.database.attributes.rdbmsspecific;

import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.GuiID;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.backend.database.JavaDataType;
import com.volavis.dbanonym.backend.database.attributes.rdbmsunspecific.StringAttribute;
import com.volavis.dbanonym.backend.database.jdbc.JdbcDao;
import com.volavis.dbanonym.backend.database.rdbms.DatabaseVersion;
import com.volavis.dbanonym.backend.database.rdbms.oracle.OracleAdditionalMetadata;

/**
 * Attribute for Oracle's character data types.
 */
public class OracleStringAttribute extends StringAttribute {

    /**
     * Byte (B) = true; Char (C) = false.
     */
    boolean usesBytes = true;

    /**
     * Constructor.
     * @param attribute Attribute whose properties will be copied.
     */
    public OracleStringAttribute(Attribute attribute) {
        super(attribute);
        setJavaDataType(JavaDataType.ORACLE_STRING);
        setGuiID(GuiID.ORACLE_CHARACTERS);
    }

    /**
     * Constructor.
     * @param dao JDBC DAO.
     * @param dbVersion The database version.
     * @param dbName Name of the database.
     * @param entity The entity the attribute belongs to.
     * @param attribute Attribute which is currently being considered.
     */
    public OracleStringAttribute(JdbcDao dao, DatabaseVersion dbVersion, String dbName, Entity entity, Attribute attribute) {
        this(attribute);
        queryQualifier(dao, dbVersion, dbName, entity, attribute);
    }

    /**
     * Queries the qualifier: Is the length in chars or bytes?
     * @param dao JDBC DAO.
     * @param dbVersion The database version.
     * @param dbName Name of the database.
     * @param entity The entity the attribute belongs to.
     * @param attribute Attribute which is currently being considered.
     */
    private void queryQualifier(JdbcDao dao, DatabaseVersion dbVersion, String dbName, Entity entity, Attribute attribute) {
        OracleAdditionalMetadata oracleAdditionalMetadata = (OracleAdditionalMetadata) dbVersion;
        String qualifierValue = oracleAdditionalMetadata.queryQualifierCharacterDataTypes(dao, dbName, entity, attribute);
        this.usesBytes = !"C".equals(qualifierValue);
    }

    @Override
    public String getDataTypeInformation() {
        if(usesBytes) {
            return getPrecision() + " " + StringConstants.ATTRIBUTE_INFO_BYTES;
        } else {
            return getPrecision() + " " + StringConstants.ATTRIBUTE_INFO_CHARS;
        }
    }

    // Getter and setter
    public boolean usesBytes() {
        return usesBytes;
    }
    public void setUsesBytes(boolean usesBytes) {
        this.usesBytes = usesBytes;
    }
}