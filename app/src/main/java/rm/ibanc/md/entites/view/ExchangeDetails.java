package rm.ibanc.md.entites.view;

/**
 * Created by Cepraga Mihail on 1/2/17.
 */
public class ExchangeDetails {
    private String valuta;
    private Double cumparare;
    private Double vinzare;
    private Double cursBnm;
    private String imageExchange;
    private Boolean imagePath;

    public Double getCumparare() {
        return cumparare;
    }

    public void setCumparare(Double cumparare) {
        this.cumparare = cumparare;
    }

    public Double getCursBnm() {
        return cursBnm;
    }

    public void setCursBnm(Double cursBnm) {
        this.cursBnm = cursBnm;
    }

    public String getImageExchange() {
        return imageExchange;
    }

    public void setImageExchange(String imageExchange) {
        this.imageExchange = imageExchange;
    }

    public Boolean getImagePath() {
        return imagePath;
    }

    public void setImagePath(Boolean imagePath) {
        this.imagePath = imagePath;
    }

    public String getValuta() {
        return valuta;
    }

    public void setValuta(String valuta) {
        this.valuta = valuta;
    }

    public Double getVinzare() {
        return vinzare;
    }

    public void setVinzare(Double vinzare) {
        this.vinzare = vinzare;
    }
}
