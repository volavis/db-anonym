package com.volavis.dbanonym.backend.anonymization.options.constoption.data;

import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionData;

import java.math.BigDecimal;

/**
 * Saves the (const) GUI user input for the GuiID Decimal.
 */
public class DecimalConstData extends ConstOptionData {

    private BigDecimal bigDecimalValue;

    @Override
    public boolean isValid() {
        return bigDecimalValue != null;
    }

    // Getter and setter
    public BigDecimal getBigDecimalValue() {
        return bigDecimalValue;
    }
    public void setBigDecimalValue(BigDecimal bigDecimalValue) {
        this.bigDecimalValue = bigDecimalValue;
    }
}