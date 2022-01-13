package ru.icmit.rtcc.models;

import java.math.BigDecimal;

public class CurrencyPriceToClient {
    private String exchange;
    private String base;
    private String currency;
    private BigDecimal amount;

    public CurrencyPriceToClient(String exchange, String base, String currency, BigDecimal amount) {
        this.exchange = exchange;
        this.base = base;
        this.currency = currency;
        this.amount = amount;
    }

    public String getExchange() {
        return exchange;
    }

    public String getBase() {
        return base;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String toString() {
        return exchange + ": " + amount;
    }
}
