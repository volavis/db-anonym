package com.volavis.dbanonym.backend.anonymization.options;

/**
 * Superclass for all the AnonymizationOptions data classes.
 * <P> The data classes save the user's GUI inputs.
 */
public abstract class OptionData {

    private final OptionID optionID;

    /**
     * Constructor.
     * @param id OptionID to identify the anonymization option with.
     */
    public OptionData(OptionID id) {
        this.optionID = id;
    }

    /**
     * Checks if the user input is valid (checks for null).
     * @return valid = true, invalid = false.
     */
    public abstract boolean isValid();

    // Getter
    public OptionID getOptionID() {
        return optionID;
    }
}