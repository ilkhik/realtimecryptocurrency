package ru.icmit.rtcc.exchanges;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import ru.icmit.rtcc.models.CurrencyPair;
import ru.icmit.rtcc.models.CurrencyPrice;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Component
public class BitfinexApi extends ExchangeApi {
    private List<CurrencyPair> availablePairs = new LinkedList<>();

    @Override
    public String getName() {
        return "Bitfinex";
    }

    @Override
    public CurrencyPrice getCurrencyPrice(CurrencyPair pair) throws ExchangeApiException {
        try {
            String response = doGetRequest(pair.getCurrencyTicker(), pair.getBase(),
                    "https://api.bitfinex.com/v1/pubticker/%s%s");
            JSONObject jsonObject = new JSONObject(response);
            BigDecimal amount = jsonObject.getBigDecimal("last_price");
            return new CurrencyPrice(amount, pair);
        } catch (IOException | JSONException e) {
            throw new ExchangeApiException();
        }
    }

    @Override
    public List<CurrencyPrice> getCurrencyPrices() throws ExchangeApiException {
        return null;
    }
}
