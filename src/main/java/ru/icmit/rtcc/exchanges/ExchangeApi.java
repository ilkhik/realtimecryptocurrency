package ru.icmit.rtcc.exchanges;

import ru.icmit.rtcc.models.CurrencyPair;
import ru.icmit.rtcc.models.CurrencyPrice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public abstract class ExchangeApi implements Runnable {
    public abstract String getName();

    protected ExchangeApi(){
        new Thread(this).start();
    }

    public abstract CurrencyPrice getCurrencyPrice(CurrencyPair pair) throws ExchangeApiException;

    public void run(){}

    protected String doGetRequest(String firstCurrency, String secondCurrency, String requestUri) throws IOException {
        URL url = new URL(String.format(requestUri, firstCurrency, secondCurrency));
        URLConnection connection = url.openConnection();
        String response = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
        return response;
    }

}
