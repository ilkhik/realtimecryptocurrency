package ru.icmit.rtcc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.icmit.rtcc.models.CurrencyPair;
import ru.icmit.rtcc.models.ExchangeCurrencyPrice;
import ru.icmit.rtcc.services.CryptoPricesService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private CryptoPricesService cryptoPricesService;

    @GetMapping("/getCryptocurrencyPrices")
    public List<ExchangeCurrencyPrice> getCryptocurrencyPrice(@RequestParam("base") String base, @RequestParam("currency") String currency) {
        try {
            return cryptoPricesService.getCryptocurrencyPrices(new CurrencyPair(currency, base));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }
}
