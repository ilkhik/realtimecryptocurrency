package ru.icmit.rtcc.models;

public class ExchangeCurrencyPrice {
    private String exchange;
    private CurrencyPrice currencyPrice;

    public ExchangeCurrencyPrice(String exchange, CurrencyPrice currencyPrice) {
        this.exchange = exchange;
        this.currencyPrice = currencyPrice;
    }

    public String getExchange() {
        return exchange;
    }

    public CurrencyPrice getCurrencyPrice() {
        return currencyPrice;
    }
}
