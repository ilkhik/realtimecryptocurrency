package ru.icmit.rtcc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.icmit.rtcc.models.CurrencyPair;
import ru.icmit.rtcc.models.CurrencyPriceToClient;
import ru.icmit.rtcc.services.CryptoPricesService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {
    private CryptoPricesService cryptoPricesService;

    @Autowired
    public ApiController(CryptoPricesService cryptoPricesService) {
        this.cryptoPricesService = cryptoPricesService;
    }

    @GetMapping(value = "/pricesByPair")
    public List<CurrencyPriceToClient> getCryptocurrencyPrice(@RequestParam("base") String base, @RequestParam("currency") String currency) {
        try {
            return cryptoPricesService.getCryptocurrencyPrices(new CurrencyPair(currency, base));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }
}
