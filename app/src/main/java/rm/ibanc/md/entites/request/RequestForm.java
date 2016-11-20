package rm.ibanc.md.entites.request;

/**
 * Created by PC01017745 on 25.10.2016.
 */
public class RequestForm {
    private String personalId;
    private String GUID;

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }
}
