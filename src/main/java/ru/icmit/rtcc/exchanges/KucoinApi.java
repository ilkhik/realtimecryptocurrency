package ru.icmit.rtcc.exchanges;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import ru.icmit.rtcc.models.CurrencyPair;
import ru.icmit.rtcc.models.CurrencyPrice;

import java.io.IOException;
import java.math.BigDecimal;

@Component
public class KucoinApi extends ExchangeApi {
    @Override
    public CurrencyPrice getCurrencyPrice(CurrencyPair pair) throws ExchangeApiException {
        try {
            String response = doGetRequest(pair.getBase(), pair.getCurrencyTicker(),
                    "https://api.kucoin.com/api/v1/prices?base=%s&currencies=%s");
            JSONObject jsonObject = new JSONObject(response);
            JSONObject data = jsonObject.getJSONObject("data");
            BigDecimal amount = data.getBigDecimal(pair.getCurrencyTicker());
            return new CurrencyPrice(amount, pair, this);
        } catch (IOException|JSONException e) {
            throw new ExchangeApiException();
        }
    }

    @Override
    public String getName() {
        return "Kucoin";
    }

    public KucoinApi() {
        super();
    }
}
