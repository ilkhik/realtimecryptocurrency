package ru.icmit.rtcc.exchanges;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import ru.icmit.rtcc.models.CurrencyPair;
import ru.icmit.rtcc.models.CurrencyPrice;

import java.io.IOException;
import java.math.BigDecimal;

@Component
public class CoinbaseApi extends ExchangeApi {
    @Override
    public String getName() {
        return "Coinbase";
    }

    @Override
    public CurrencyPrice getCurrencyPrice(CurrencyPair pair) throws ExchangeApiException {
        try {
            String response = doGetRequest(pair.getCurrencyTicker(), pair.getBase(),
                    "https://api.coinbase.com/v2/prices/%s-%s/buy");
            JSONObject jsonObject = new JSONObject(response);
            JSONObject data = jsonObject.getJSONObject("data");
            BigDecimal amount = data.getBigDecimal("amount");
            return new CurrencyPrice(amount, pair, this);
        } catch (IOException|JSONException e) {
            throw new ExchangeApiException();
        }
    }
}
