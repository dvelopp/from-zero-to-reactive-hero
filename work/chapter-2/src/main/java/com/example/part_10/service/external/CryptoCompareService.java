package com.example.part_10.service.external;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

import com.example.part_10.service.CryptoService;
import com.example.part_10.service.external.utils.PriceMessageUnpacker;
import com.example.part_10.service.external.utils.TradeMessageUnpacker;

public class CryptoCompareService implements CryptoService {
    public static final int CACHE_SIZE = 3;

    private final Flux<Map<String, Object>> reactiveCryptoListener;

    public CryptoCompareService() {
        reactiveCryptoListener = CryptoCompareClient
                .connect(
                        Flux.just("5~CCCAGG~BTC~USD", "0~Coinbase~BTC~USD", "0~Cexio~BTC~USD"),
                        Arrays.asList(new PriceMessageUnpacker(), new TradeMessageUnpacker())
                )
                .transform(CryptoCompareService::provideResilience)
                .transform(CryptoCompareService::provideCaching);
    }

    public Flux<Map<String, Object>> eventsStream() {
        return reactiveCryptoListener;
    }

    public static <T> Flux<T> provideResilience(Flux<T> input) {
        return input.retryBackoff(100, Duration.ofMillis(100));
    }


    public static <T> Flux<T> provideCaching(Flux<T> input) {
        return input.replay(3).autoConnect(0);
    }

}
