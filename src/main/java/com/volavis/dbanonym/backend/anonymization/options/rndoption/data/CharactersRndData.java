package com.volavis.dbanonym.backend.anonymization.options.rndoption.data;

import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionData;

/**
 * Saves the (rnd) GUI user input for the GuiID Characters.
 */
public class CharactersRndData extends RndOptionData {

    private CharactersDropdownElement charactersDropdownElement = CharactersDropdownElement.SAME_LENGTH;
    private Integer minLength;
    private Integer maxLength;

    @Override
    public boolean isValid() {
        if(charactersDropdownElement == CharactersDropdownElement.USER_DEFINED) {
            return minLength != null && maxLength != null;
        }
        return true;
    }

    // Getter and setter
    public CharactersDropdownElement getCharactersDropdownElement() {
        return charactersDropdownElement;
    }
    public void setCharactersDropdownElement(CharactersDropdownElement charactersDropdownElement) {
        this.charactersDropdownElement = charactersDropdownElement;
    }
    public Integer getMinLength() {
        return minLength;
    }
    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }
    public Integer getMaxLength() {
        return maxLength;
    }
    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * Enum for the dropdown elements of the Characters gui.
     */
    public enum CharactersDropdownElement {

        SAME_LENGTH(StringConstants.RND_CHARACTERS_DROPDOWN_ELEMENT_SAME_LENGTH_TITLE, StringConstants.RND_CHARACTERS_DROPDOWN_ELEMENT_SAME_LENGTH_INFO),
        USER_DEFINED(StringConstants.RND_CHARACTERS_DROPDOWN_ELEMENT_USER_DEFINED_TITLE, StringConstants.RND_CHARACTERS_DROPDOWN_ELEMENT_USER_DEFINED_INFO);

        private final String dropdownTitle;
        private final String infoText;

        /**
         * Constructor.
         * @param dropdownTitle Title shown in the dropdown component.
         * @param infoText Information text shown beneath the dropdown component.
         */
        CharactersDropdownElement(String dropdownTitle, String infoText) {
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