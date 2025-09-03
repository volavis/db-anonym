package com.volavis.dbanonym.backend.anonymization.options;

import com.volavis.dbanonym.backend.anonymization.ValueWithPKs;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.ConnectedDatabase;
import com.volavis.dbanonym.backend.database.Entity;

import java.util.List;

/**
 * Superclass for all the anonymization options.
 */
public abstract class AnonymizationOption {

    private final OptionID optionID;
    private final boolean usesPKValues;

    /**
     * Constructor.
     * @param optionID The option's ID.
     * @param usesPKValues Does the option use PK values in its update method?
     */
    public AnonymizationOption(OptionID optionID, boolean usesPKValues) {
        this.optionID = optionID;
        this.usesPKValues = usesPKValues;
    }

    /**
     * Returns the anonymization option's data class object.
     * @param attribute Attribute that holds the GuiID.
     */
    public abstract OptionData getOptionData(Attribute attribute);

    /**
     * Returns the anonymization option's GUI class object.
     * @param attribute The GUI will be build based on the attribute's properties.
     * @param entity Entity the attribute belongs to.
     * @param optionData Data class that saves the user input.
     */
    public abstract OptionComponent getOptionComponent(OptionData optionData, Entity entity, Attribute attribute);

    /**
     * Defines the procedure how this AnonymizationOption anonymizes.
     * @param database Database the user connected to.
     * @param entity Entity the attribute belongs to.
     * @param attribute Attribute that will be anonymized.
     * @param pkList List of all primary key attributes in the table.
     * @param valueWithPKsList List of ValueWithPKs objects. Contain the values and the pk values.
     */
    public abstract void update(ConnectedDatabase database, Entity entity, Attribute attribute,
                                List<Attribute> pkList, List<ValueWithPKs> valueWithPKsList);

    /**
     * Enables or disables the AnonymizationOption's radio button in the AnonymizationView.
     * <P>Disables the radio button if the AnonymizationOption cannot be used with the passed attribute.
     * @param attribute Attribute for which the user selects an AnonymizationOption.
     * @return Enable = true, disable = false.
     */
    public abstract boolean enableRadioButton(Attribute attribute);

    // Getter
    public OptionID getOptionID() {
        return optionID;
    }
    public boolean usesPKValues() {
        return usesPKValues;
    }
}