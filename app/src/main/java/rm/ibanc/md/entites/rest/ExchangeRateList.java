package rm.ibanc.md.entites.rest;

import java.util.Date;

/**
 * Created by victor.zaitev on 12.12.2016.
 */
public class ExchangeRateList {

    private String valutaShortName;
    private Date dateCurs;
    private double officialCurs;
    private double buyCurs;
    private double sellCurs;
    private Boolean imagePath;

    public String getValutaShortName() {
        return valutaShortName;
    }

    public void setValutaShortName(String valutaShortName) {
        this.valutaShortName = valutaShortName;
    }

    public Date getDateCurs() {
        return dateCurs;
    }

    public void setDateCurs(Date dateCurs) {
        this.dateCurs = dateCurs;
    }

    public double getOfficialCurs() {
        return officialCurs;
    }

    public void setOfficialCurs(double officialCurs) {
        this.officialCurs = officialCurs;
    }

    public double getBuyCurs() {
        return buyCurs;
    }

    public void setBuyCurs(double buyCurs) {
        this.buyCurs = buyCurs;
    }

    public double getSellCurs() {
        return sellCurs;
    }

    public void setSellCurs(double sellCurs) {
        this.sellCurs = sellCurs;
    }

    public Boolean getImagePath() {
        return imagePath;
    }

    public void setImagePath(Boolean imagePath) {
        this.imagePath = imagePath;
    }
}
