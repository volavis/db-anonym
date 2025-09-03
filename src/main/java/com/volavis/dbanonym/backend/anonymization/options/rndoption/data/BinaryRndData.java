package com.volavis.dbanonym.backend.anonymization.options.rndoption.data;

import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionData;

/**
 * Saves the (rnd) GUI user input for the GuiID Binary.
 */
public class BinaryRndData extends RndOptionData {

    private BinaryDropdownElement binaryDropdownElement = BinaryDropdownElement.SAME_BYTE_COUNT;
    private Integer minBytes;
    private Integer maxBytes;

    @Override
    public boolean isValid() {
        if(binaryDropdownElement == BinaryDropdownElement.USER_DEFINED) {
            return minBytes != null && maxBytes != null;
        }
        return true;
    }

    // Getter and setter
    public BinaryDropdownElement getBinaryDropdownElement() {
        return binaryDropdownElement;
    }
    public void setBinaryDropdownElement(BinaryDropdownElement binaryDropdownElement) {
        this.binaryDropdownElement = binaryDropdownElement;
    }
    public Integer getMinBytes() {
        return minBytes;
    }
    public void setMinBytes(Integer minBytes) {
        this.minBytes = minBytes;
    }
    public Integer getMaxBytes() {
        return maxBytes;
    }
    public void setMaxBytes(Integer maxBytes) {
        this.maxBytes = maxBytes;
    }

    /**
     * Enum for the dropdown elements of the Binary gui.
     */
    public enum BinaryDropdownElement {

        SAME_BYTE_COUNT(StringConstants.RND_BINARY_DROPDOWN_ELEMENT_SAME_BYTE_COUNT_TITLE, StringConstants.RND_BINARY_DROPDOWN_ELEMENT_SAME_BYTE_COUNT_INFO),
        USER_DEFINED(StringConstants.RND_BINARY_DROPDOWN_ELEMENT_USER_DEFINED_TITLE, StringConstants.RND_BINARY_DROPDOWN_ELEMENT_USER_DEFINED_INFO);

        private final String dropdownTitle;
        private final String infoText;

        /**
         * Constructor.
         * @param dropdownTitle Title shown in the dropdown component.
         * @param infoText Information text shown beneath the dropdown component.
         */
        BinaryDropdownElement(String dropdownTitle, String infoText) {
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