/*
 * Copyright (c) 2018 Nextiva, Inc. to Present.
 * All rights reserved.
 */
package com.example.part_11.service;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Map;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.part_11.service.utils.PriceMessageUnpacker;
import com.example.part_11.service.utils.TradeMessageUnpacker;

import static java.util.Arrays.asList;

@Service
public class CryptoServiceImpl implements CryptoService {

    private static final int CACHE_SIZE = 3;
    private static final int NUM_RETRIES = 100;

    @Autowired
    private CryptoCompareClient client;
    @Autowired
    private PriceMessageUnpacker priceMessageUnpacker;
    @Autowired
    private TradeMessageUnpacker tradeMessageUnpacker;

    private Flux<Map<String, Object>> reactiveCryptoListener;

    @PostConstruct
    public void init() {
        reactiveCryptoListener = client.connect(
                Flux.just("5~CCCAGG~BTC~USD", "0~Coinbase~BTC~USD", "0~Cexio~BTC~USD"),
                asList(priceMessageUnpacker, tradeMessageUnpacker))
                .transform(this::provideResilience)
                .transform(this::provideCaching);
    }

    @Override
    public Flux<Map<String, Object>> eventsStream() {
        return reactiveCryptoListener;
    }

    public <T> Flux<T> provideResilience(Flux<T> input) {
        return input.retryBackoff(NUM_RETRIES, Duration.ofMillis(100));
    }

    public <T> Flux<T> provideCaching(Flux<T> input) {
        return input.replay(CACHE_SIZE).autoConnect(0);
    }

}
