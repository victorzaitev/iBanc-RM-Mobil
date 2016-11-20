package rm.ibanc.md.CustomersRest;

import java.util.Date;

import rm.ibanc.md.entites.rest.BaseEntity;

/**
 * Created by PC01017745 on 10.08.2016.
 */
public class CustomersRest extends BaseEntity {

    private int id;

    private short shortId;


    private String name;
    private String firstname;

    private String adress;
    private String login;

    private Date lastUpdateDate;

    public CustomersRest() {
    }

    public CustomersRest(int id, short shortId, String name, String firstname, String adress, String login, Date lastUpdateDate) {
        this.id = id;
        this.shortId = shortId;
        this.name = name;
        this.firstname = firstname;
        this.adress = adress;
        this.login = login;
        this.lastUpdateDate = lastUpdateDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public short getShortId() {
        return shortId;
    }

    public void setShortId(short shortId) {
        this.shortId = shortId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
