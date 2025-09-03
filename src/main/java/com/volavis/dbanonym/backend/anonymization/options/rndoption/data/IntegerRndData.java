package com.volavis.dbanonym.backend.anonymization.options.rndoption.data;

import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionData;

import java.math.BigInteger;

/**
 * Saves the (rnd) GUI user input for the GuiID Integer.
 */
public class IntegerRndData extends RndOptionData {

    private IntegerDropdownElement integerDropdownElement = IntegerDropdownElement.SAME_DIGIT_COUNT;
    private boolean useSign = true;
    private BigInteger minInteger;
    private BigInteger maxInteger;

    @Override
    public boolean isValid() {
        if(integerDropdownElement == IntegerDropdownElement.USER_DEFINED) {
            return minInteger != null && maxInteger != null;
        }
        return true;
    }

    // Getter and setter
    public IntegerDropdownElement getIntegerDropdownElement() {
        return integerDropdownElement;
    }
    public void setIntegerDropdownElement(IntegerDropdownElement integerDropdownElement) {
        this.integerDropdownElement = integerDropdownElement;
    }
    public boolean useSign() {
        return useSign;
    }
    public void setUseSign(boolean useSign) {
        this.useSign = useSign;
    }
    public BigInteger getMinInteger() {
        return minInteger;
    }
    public void setMinInteger(BigInteger minInteger) {
        this.minInteger = minInteger;
    }
    public BigInteger getMaxInteger() {
        return maxInteger;
    }
    public void setMaxInteger(BigInteger maxInteger) {
        this.maxInteger = maxInteger;
    }

    /**
     * Enum for the dropdown elements of the Integer gui.
     */
    public enum IntegerDropdownElement {

        SAME_DIGIT_COUNT(StringConstants.RND_INTEGER_DROPDOWN_ELEMENT_SAME_DIGIT_COUNT_TITLE, StringConstants.RND_INTEGER_DROPDOWN_ELEMENT_SAME_DIGIT_COUNT_INFO),
        VALUE_RANGE(StringConstants.RND_INTEGER_DROPDOWN_ELEMENT_VALUE_RANGE_TITLE, StringConstants.RND_INTEGER_DROPDOWN_ELEMENT_VALUE_RANGE_INFO),
        USER_DEFINED(StringConstants.RND_INTEGER_DROPDOWN_ELEMENT_USER_DEFINED_TITLE, StringConstants.RND_INTEGER_DROPDOWN_ELEMENT_USER_DEFINED_INFO);

        private final String dropdownTitle;
        private final String infoText;

        /**
         * Constructor.
         * @param dropdownTitle Title shown in the dropdown component.
         * @param infoText Information text shown beneath the dropdown component.
         */
        IntegerDropdownElement(String dropdownTitle, String infoText) {
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