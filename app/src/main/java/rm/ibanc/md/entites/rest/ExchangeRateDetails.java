package rm.ibanc.md.entites.rest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor.zaitev on 12.12.2016.
 */
public class ExchangeRateDetails extends BaseEntity {
    public List<ExchangeRateList> exchangeRateList = new ArrayList<>();

    public List<ExchangeRateList> getExchangeRateList() {
        return exchangeRateList;
    }

    public void setExchangeRateList(List<ExchangeRateList> exchangeRateList) {
        this.exchangeRateList = exchangeRateList;
    }


}
