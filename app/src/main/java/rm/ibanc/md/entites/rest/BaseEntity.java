package rm.ibanc.md.entites.rest;

/**
 * Created by victor.zaitev on 29.09.2016.
 */
public class BaseEntity {

    private int returnCode;

    private String returnDescription;

    private String token;

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnDescription() {
        return returnDescription;
    }

    public void setReturnDescription(String returnDescription) {
        this.returnDescription = returnDescription;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
