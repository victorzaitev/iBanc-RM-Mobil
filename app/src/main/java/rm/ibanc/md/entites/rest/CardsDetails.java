package rm.ibanc.md.entites.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC01017745 on 25.10.2016.
 */
public class CardsDetails extends BaseEntity implements Serializable {

    public List<CardsList> cardsList = new ArrayList<>();

    public List<CardsList> getCardsList() {
        return cardsList;
    }

    public void setCardsList(List<CardsList> cardsList) {
        this.cardsList = cardsList;
    }
}
