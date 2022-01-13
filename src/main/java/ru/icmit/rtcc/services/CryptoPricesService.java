package ru.icmit.rtcc.services;

import ru.icmit.rtcc.models.CurrencyPair;
import ru.icmit.rtcc.models.CurrencyPriceToClient;

import java.util.List;

public interface CryptoPricesService {
    List<CurrencyPriceToClient> getCryptocurrencyPrices(CurrencyPair pair);
}
