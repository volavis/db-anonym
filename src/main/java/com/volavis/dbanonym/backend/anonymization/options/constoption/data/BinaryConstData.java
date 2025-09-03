package com.volavis.dbanonym.backend.anonymization.options.constoption.data;

import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionData;

import java.nio.charset.StandardCharsets;

/**
 * Saves the (const) GUI user input for the GuiID Binary.
 */
public class BinaryConstData extends ConstOptionData {

    private byte[] byteArray;

    @Override
    public boolean isValid() {
        return byteArray != null;
    }

    /**
     * Returns the attribute byteArray as a String.
     */
    public String getStringFromByteArray() {
        if(this.byteArray != null) {
            return new String(this.byteArray, StandardCharsets.UTF_8);
        } else {
            return null;
        }
    }

    /**
     * Turns the given String into a byte[] and inserts it into the attribute byteArray.
     * @param str String that will be turned into a byte[].
     */
    public void setByteArrayFromString(String str) {
        if(str != null) {
            this.byteArray = str.getBytes(StandardCharsets.UTF_8);
        } else {
            this.byteArray = null;
        }
    }

    // Getter and setter
    public byte[] getByteArray() {
        return byteArray;
    }
    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }
}