package com.volavis.dbanonym.backend.anonymization.options.rndoption.data;

import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionData;

import java.math.BigDecimal;

/**
 * Saves the (rnd) GUI user input for the GuiID Decimal.
 */
public class DecimalRndData extends RndOptionData {

    private DecimalDropdownElement decimalDropdownElement = DecimalDropdownElement.SAME_PRE_POST_DECIMAL_PLACES;
    private boolean useSign = true;
    private Integer decimalPlace;
    private BigDecimal minDecimal;
    private BigDecimal maxDecimal;

    @Override
    public boolean isValid() {
        if(decimalDropdownElement == DecimalDropdownElement.USER_DEFINED) {
            return decimalPlace != null && minDecimal != null && maxDecimal != null;
        }
        return true;
    }

    // Getter and setter
    public DecimalDropdownElement getDecimalDropdownElement() {
        return decimalDropdownElement;
    }
    public void setDecimalDropdownElement(DecimalDropdownElement decimalDropdownElement) {
        this.decimalDropdownElement = decimalDropdownElement;
    }
    public boolean useSign() {
        return useSign;
    }
    public void setUseSign(boolean useSign) {
        this.useSign = useSign;
    }
    public Integer getDecimalPlace() {
        return decimalPlace;
    }
    public void setDecimalPlace(Integer decimalPlace) {
        this.decimalPlace = decimalPlace;
    }
    public BigDecimal getMinDecimal() {
        return minDecimal;
    }
    public void setMinDecimal(BigDecimal minDecimal) {
        this.minDecimal = minDecimal;
    }
    public BigDecimal getMaxDecimal() {
        return maxDecimal;
    }
    public void setMaxDecimal(BigDecimal maxDecimal) {
        this.maxDecimal = maxDecimal;
    }

    /**
     * Enum for the dropdown elements of the Decimal gui.
     */
    public enum DecimalDropdownElement {

        SAME_PRE_POST_DECIMAL_PLACES(StringConstants.RND_DECIMAL_DROPDOWN_ELEMENT_SAME_LENGTH_TITLE, StringConstants.RND_DECIMAL_DROPDOWN_ELEMENT_SAME_LENGTH_INFO),
        USER_DEFINED(StringConstants.RND_DECIMAL_DROPDOWN_ELEMENT_USER_DEFINED_TITLE, StringConstants.RND_DECIMAL_DROPDOWN_ELEMENT_USER_DEFINED_INFO);

        private final String dropdownTitle;
        private final String infoText;

        /**
         * Constructor.
         * @param dropdownTitle Title shown in the dropdown component.
         * @param infoText Information text shown beneath the dropdown component.
         */
        DecimalDropdownElement(String dropdownTitle, String infoText) {
            this.dropdownTitle = dropdownTitle;
            this.infoText = infoText;
        }

        @Override
        public String toString() {
            return this.dropdownTitle;
        }
        public String getInfoText() {
            return infoText;
        }
    }
}