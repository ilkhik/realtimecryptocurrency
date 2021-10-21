package ru.icmit.rtcc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.icmit.rtcc.exchanges.ExchangeApi;
import ru.icmit.rtcc.models.CurrencyPair;
import ru.icmit.rtcc.models.CurrencyPrice;
import ru.icmit.rtcc.models.ExchangeCurrencyPrice;

import java.util.LinkedList;
import java.util.List;

@Component
public class CryptoPricesServiceImpl implements CryptoPricesService {
    private List<ExchangeApi> exchanges;

    @Autowired
    public CryptoPricesServiceImpl(List<ExchangeApi> exchangeApi) {
        this.exchanges = exchangeApi;
    }

    @Override
    public List<ExchangeCurrencyPrice> getCryptocurrencyPrices(CurrencyPair pair) {
        List<ExchangeCurrencyPrice> prices = new LinkedList<>();
        for (ExchangeApi exchange : exchanges) {
            try {
                CurrencyPrice price = exchange.getCurrencyPrice(pair);
                prices.add(new ExchangeCurrencyPrice(exchange.getName(), price));
            } catch (ru.icmit.rtcc.exchanges.ExchangeApiException e) {
                e.printStackTrace();
            }
        }
        return prices;
    }
}
