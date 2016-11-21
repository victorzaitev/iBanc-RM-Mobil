package rm.ibanc.md.entites.rest;

import java.io.Serializable;

/**
 * Created by PC01017745 on 25.10.2016.
 */
public class CardsList implements Serializable {

    private String types;
    private String status;
    private String pan;
    private String description;
    private String maskPan;



    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaskPan() {
        return maskPan;
    }

    public void setMaskPan(String maskPan) {
        this.maskPan = maskPan;
    }
}
