package ru.icmit.rtcc.models;

import ru.icmit.rtcc.exchanges.ExchangeApi;

import java.math.BigDecimal;

public class CurrencyPrice {
    private final CurrencyPair currencyPair;
    private BigDecimal amount;

    public CurrencyPrice(BigDecimal amount, CurrencyPair currencyPair, ExchangeApi exchange) {
        this.currencyPair = currencyPair;
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public synchronized void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public CurrencyPair getCurrencyPair() {
        return currencyPair;
    }
}
