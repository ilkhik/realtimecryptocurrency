package ru.icmit.rtcc.services;

import ru.icmit.rtcc.models.CurrencyPair;
import ru.icmit.rtcc.models.ExchangeCurrencyPrice;

import java.util.List;

public interface CryptoPricesService {
    List<ExchangeCurrencyPrice> getCryptocurrencyPrices(CurrencyPair pair);
}
