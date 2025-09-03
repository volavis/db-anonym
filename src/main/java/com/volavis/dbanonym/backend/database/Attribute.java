package com.volavis.dbanonym.backend.database;

import com.volavis.dbanonym.backend.anonymization.additionaldata.AnonymizationDataID;
import com.volavis.dbanonym.backend.anonymization.options.GuiID;
import com.volavis.dbanonym.backend.anonymization.options.OptionData;
import com.volavis.dbanonym.backend.database.metadata.ForeignKey;

import java.util.*;

/**
 * Attribute (column) of a database entity.
 */
public abstract class Attribute {

    private String name;
    private String databaseDataType;
    private int jdbcDataTypeDriver;             // JDBC Datatype from JDBC driver
    private int jdbcDataTypeValidation;         // JDBC Datatype used for validation
    private int jdbcDataType;                   // JDBC Datatype used for update query
    private String javaClassNameDriver;         // Java class name returned by JDBC driver
    private JavaDataType javaDataType;
    private boolean useJavaDataTypeFallback;    // True when javaDataType is UNKNOWN; uses javaClassNameDriver instead
    private GuiID guiID;
    private int precision;                      // E.g Characters: char length; Integer: digit length; Binary: byte size
    private int scale;                          // Number of digits to the right of the decimal point
    private boolean isPK;
    private ForeignKey foreignKey;
    private boolean isNullable;
    private boolean isUnique;
    private String uniqueConstraintName;
    private boolean isSigned = true;
    private String defaultValue;
    private Set<String> possibleCheckConstraints = new HashSet<>();
    private OptionData anonymizationOptionData;

    /**
     * Returns the attribute's data type information (no useful info == empty String).
     */
    public String getDataTypeInformation() {
        return "";
    }

    /**
     * Returns the constant value used for the anonymization.
     */
    public Object getConstValue() {
        throw new IllegalStateException("The attribute you are trying to anonymize has to override the getConstValue() method!");
    }

    /**
     * Returns the random value used for the anonymization.
     * @param additionalDataMap Map containing additional metadata used in anonymization.
     */
    public Object getRndValue(Map<AnonymizationDataID, Object> additionalDataMap) {
        throw new IllegalStateException("The attribute you are trying to anonymize has to override the getRndValue() method!");
    }

    /**
     * Constructor.
     * @param javaDataType The new JavaDataType.
     * @param guiID The new GuiID.
     */
    public Attribute(JavaDataType javaDataType, GuiID guiID) {
        this.javaDataType = javaDataType;
        this.guiID = guiID;
    }

    /**
     * Constructor.
     * @param attribute Attribute whose properties will be copied.
     * @param javaDataType The new JavaDataType.
     * @param guiID The new GuiID.
     */
    public Attribute(Attribute attribute, JavaDataType javaDataType, GuiID guiID) {
        this(attribute);
        this.javaDataType = javaDataType;
        this.guiID = guiID;
    }

    /**
     * Copy constructor.
     * <P> AnonymizationOptionData won't be copied.
     * @param attribute Attribute whose properties will be copied.
     */
    private Attribute(Attribute attribute) {
        if(attribute != null) {
            this.name = attribute.name;
            this.databaseDataType = attribute.databaseDataType;
            this.jdbcDataTypeDriver = attribute.jdbcDataTypeDriver;
            this.jdbcDataTypeValidation = attribute.jdbcDataTypeValidation;
            this.jdbcDataType = attribute.jdbcDataType;
            this.javaClassNameDriver = attribute.javaClassNameDriver;
            this.javaDataType = attribute.javaDataType;
            this.useJavaDataTypeFallback = attribute.useJavaDataTypeFallback;
            this.guiID = attribute.guiID;
            this.precision = attribute.precision;
            this.scale = attribute.scale;
            this.isPK = attribute.isPK;
            this.isNullable = attribute.isNullable;
            this.isUnique = attribute.isUnique;
            this.uniqueConstraintName = attribute.uniqueConstraintName;
            this.isSigned = attribute.isSigned;
            this.defaultValue = attribute.defaultValue;
            this.possibleCheckConstraints = attribute.possibleCheckConstraints;

            if(attribute.foreignKey != null) {
                this.foreignKey = new ForeignKey(
                        attribute.foreignKey.getColumnName(),
                        attribute.foreignKey.getRefTableName(),
                        attribute.foreignKey.getRefColumnName()
                );
            }

            this.anonymizationOptionData = null;
        }
    }

    /**
     * Checks if the anonymization option data is valid.
     * @return valid = true, invalid = false;
     */
    public boolean isValid() {
        if (anonymizationOptionData != null) {
            return anonymizationOptionData.isValid();
        }
        return true;
    }

    /**
     * Adds a possible check constraint to the possibleCheckConstraints set.
     * @param constraint Constraint that will be added.
     */
    public void addPossibleCheckConstraint(String constraint) {
        possibleCheckConstraints.add(constraint);
    }

    // Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatabaseDataType() {
        return databaseDataType;
    }

    public void setDatabaseDataType(String databaseDataType) {
        this.databaseDataType = databaseDataType;
    }

    public int getJdbcDataTypeDriver() {
        return jdbcDataTypeDriver;
    }

    public void setJdbcDataTypeDriver(int jdbcDataTypeDriver) {
        this.jdbcDataTypeDriver = jdbcDataTypeDriver;
    }

    public int getJdbcDataTypeValidation() {
        return jdbcDataTypeValidation;
    }

    public void setJdbcDataTypeValidation(int jdbcDataTypeValidation) {
        this.jdbcDataTypeValidation = jdbcDataTypeValidation;
    }

    public int getJdbcDataType() {
        return jdbcDataType;
    }

    public void setJdbcDataType(int jdbcDataType) {
        this.jdbcDataType = jdbcDataType;
    }

    public String getJavaClassNameDriver() {
        return javaClassNameDriver;
    }

    public void setJavaClassNameDriver(String javaClassNameDriver) {
        this.javaClassNameDriver = javaClassNameDriver;
    }

    public JavaDataType getJavaDataType() {
        return javaDataType;
    }

    public void setJavaDataType(JavaDataType javaDataType) {
        this.javaDataType = javaDataType;
    }

    public boolean useJavaDataTypeFallback() {
        return useJavaDataTypeFallback;
    }

    public void setUseJavaDataTypeFallback(boolean useJavaDataTypeFallback) {
        this.useJavaDataTypeFallback = useJavaDataTypeFallback;
    }

    public GuiID getGuiID() {
        return guiID;
    }

    public void setGuiID(GuiID guiID) {
        this.guiID = guiID;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public boolean isPK() {
        return isPK;
    }

    public void setPK(boolean PK) {
        isPK = PK;
    }

    public ForeignKey getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(ForeignKey foreignKey) {
        this.foreignKey = foreignKey;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public void setNullable(boolean nullable) {
        isNullable = nullable;
    }

    public boolean isUnique() {
        return isUnique;
    }

    public void setUnique(boolean unique) {
        isUnique = unique;
    }

    public String getUniqueConstraintName() {
        return uniqueConstraintName;
    }

    public void setUniqueConstraintName(String uniqueConstraintName) {
        this.uniqueConstraintName = uniqueConstraintName;
    }

    public boolean isSigned() {
        return isSigned;
    }

    public void setSigned(boolean signed) {
        isSigned = signed;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Set<String> getPossibleCheckConstraints() {
        return possibleCheckConstraints;
    }

    public OptionData getAnonymizationOptionData() {
        return anonymizationOptionData;
    }

    public void setAnonymizationOptionData(OptionData anonymizationOptionData) {
        this.anonymizationOptionData = anonymizationOptionData;
    }
}