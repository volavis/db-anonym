package com.volavis.dbanonym.backend.anonymization.options.constoption.data;

import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionData;

import java.math.BigInteger;

/**
 * Saves the (const) GUI user input for the GuiID Integer.
 */
public class IntegerConstData extends ConstOptionData {

    private BigInteger bigIntegerValue;

    @Override
    public boolean isValid() {
        return bigIntegerValue != null;
    }

    // Getter and setter
    public BigInteger getBigIntegerValue() {
        return bigIntegerValue;
    }
    public void setBigIntegerValue(BigInteger bigIntegerValue) {
        this.bigIntegerValue = bigIntegerValue;
    }
}