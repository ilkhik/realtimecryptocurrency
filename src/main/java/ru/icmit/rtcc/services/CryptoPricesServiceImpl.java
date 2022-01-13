package ru.icmit.rtcc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.icmit.rtcc.exchanges.ExchangeApi;
import ru.icmit.rtcc.exchanges.ExchangeApiException;
import ru.icmit.rtcc.models.CurrencyPair;
import ru.icmit.rtcc.models.CurrencyPrice;
import ru.icmit.rtcc.models.CurrencyPriceToClient;

import javax.annotation.PreDestroy;
import java.util.*;

@Service
public class CryptoPricesServiceImpl implements CryptoPricesService {
    private List<ExchangeApi> exchanges;
    private volatile Map<CurrencyPair, List<CurrencyPriceToClient>> pricesByPair;
    private Map<CurrencyPair, List<CurrencyPriceToClient>> pricesByPairBuf0 = new HashMap<>();
    private Map<CurrencyPair, List<CurrencyPriceToClient>> pricesByPairBuf1 = new HashMap<>();
    private int currentPricesPairBuf = 0;
    private Set<CurrencyPair> requiredPairs = new HashSet<>();
    private Map<CurrencyPair, Long> pairLastRequested = new HashMap<>();
    private static final long REMOVE_NON_REQUIRED_PAIR = 60000;
    private static final long SLEEPING_TIME = 5000;
    private volatile boolean isStopped = false;
    private final Object dataUpdated = new Object();

    @Autowired
    public CryptoPricesServiceImpl(List<ExchangeApi> exchanges) {
        this.exchanges = exchanges;
        pricesByPair = pricesByPairBuf0;
        Thread t = new Thread(this::startService);
        t.setName("CryptoPricesServiceThread");
        t.setDaemon(true);
        t.start();
    }

    @Override
    public List<CurrencyPriceToClient> getCryptocurrencyPrices(CurrencyPair pair) {
        List<CurrencyPriceToClient> prices;
        pairLastRequested.put(pair, System.currentTimeMillis());
        requiredPairs.add(pair);

        if ((prices = pricesByPair.get(pair)) != null) {
            return prices;
        }

        synchronized (this) {
            notifyAll();
        }

        synchronized (dataUpdated) {
            try {
                dataUpdated.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        prices = pricesByPair.get(pair);
        if (prices == null)
            return new LinkedList<>();

        return prices;
    }

    private void startService() {
        while (!(isStopped || Thread.currentThread().isInterrupted())) {
            long processingTime = System.currentTimeMillis();

            Map<CurrencyPair, List<CurrencyPriceToClient>> currentBuf =
                    (currentPricesPairBuf == 0) ? pricesByPairBuf1 : pricesByPairBuf0;

            List<Thread> threads = new LinkedList<>();
            for (CurrencyPair pair : requiredPairs) {
                currentBuf.put(pair, new LinkedList<>());
                for (ExchangeApi exchange : exchanges) {
                    Thread t = new Thread(() -> {
                        try {
                            CurrencyPrice currencyPrice= exchange.getCurrencyPrice(pair);
                            CurrencyPriceToClient price = new CurrencyPriceToClient(exchange.getName(),
                                    currencyPrice.getCurrencyPair().getBase(),
                                    currencyPrice.getCurrencyPair().getCurrencyTicker(),
                                    currencyPrice.getAmount());

                            synchronized (currentBuf) {
                                currentBuf.get(pair).add(price);
                            }
                        } catch (ExchangeApiException e) {
//                            e.printStackTrace();
                        }
                    });

                    threads.add(t);
                    t.setDaemon(true);
                    t.start();
                }
            }

            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (Map.Entry<CurrencyPair, List<CurrencyPriceToClient>> entry : currentBuf.entrySet()) {
                entry.getValue().sort((o1, o2) -> o1.getExchange().compareTo(o2.getExchange()));
            }

            for (Map.Entry<CurrencyPair, Long> entry : pairLastRequested.entrySet()) {
                if (System.currentTimeMillis() - entry.getValue() > REMOVE_NON_REQUIRED_PAIR) {
                    requiredPairs.remove(entry.getKey());
                    pricesByPair.remove(entry.getKey());
                }
            }

            // swap buffers
            if (currentPricesPairBuf == 0) {
                currentPricesPairBuf = 1;
                pricesByPair = pricesByPairBuf1;
            } else {
                currentPricesPairBuf = 0;
                pricesByPair = pricesByPairBuf0;
            }

            synchronized (dataUpdated) {
                dataUpdated.notifyAll();
            }

            processingTime = System.currentTimeMillis() - processingTime;
            if (processingTime < SLEEPING_TIME) {
                synchronized (this) {
                    try {
                        wait(SLEEPING_TIME - processingTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @PreDestroy
    private void stopService() {
        isStopped = true;
    }

    @Override
    protected void finalize() throws Throwable {
        stopService();
    }
}
