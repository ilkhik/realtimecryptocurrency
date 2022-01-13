package ru.icmit.rtcc.models;

public class CurrencyPair {
    private final String currencyTicker;
    private final String base;

    public CurrencyPair(String currencyTicker, String base) {
        this.currencyTicker = currencyTicker.toUpperCase();
        this.base = base.toUpperCase();
    }

    public String getCurrencyTicker() {
        return currencyTicker;
    }

    @Override
    public String toString() {
        return currencyTicker + base;
    }

    public String getBase() {
        return base;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CurrencyPair))
            return false;

        return currencyTicker.equals(( (CurrencyPair)o).currencyTicker ) && base.equals( ( (CurrencyPair)o).base );
    }

    @Override
    public int hashCode() {
        return currencyTicker.hashCode() * base.hashCode();
    }
}
