package com.volavis.dbanonym.backend.anonymization.additionaldata.listbuilder;

/**
 * Class for additional decimal data.
 * <P>Contains the value's sign and its pre/ post decimal point length.
 */
public class DecimalData {

    private Boolean sign;
    private Integer preLength;
    private Integer postLength;

    public Boolean getSign() {
        return sign;
    }
    public void setSign(Boolean sign) {
        this.sign = sign;
    }
    public Integer getPreLength() {
        return preLength;
    }
    public void setPreLength(Integer preLength) {
        this.preLength = preLength;
    }
    public Integer getPostLength() {
        return postLength;
    }
    public void setPostLength(Integer postLength) {
        this.postLength = postLength;
    }
}