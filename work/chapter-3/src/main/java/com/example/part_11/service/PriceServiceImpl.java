/*
 * Copyright (c) 2018 Nextiva, Inc. to Present.
 * All rights reserved.
 */
package com.example.part_11.service;

import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.part_11.dto.MessageDTO;

@Service
public class PriceServiceImpl implements PriceService {

    private static final Logger logger = Logger.getLogger("price-service");

    @Autowired
    private CryptoService cryptoService;

    private Flux<MessageDTO<Float>> sharedStream;

    @PostConstruct
    public void init() {
        sharedStream = cryptoService.eventsStream()
                .doOnNext(event -> logger.fine("Incoming event: " + event))
                .transform(this::selectOnlyPriceUpdateEvents)
                .transform(this::currentPrice)
                .doOnNext(event -> logger.fine("Price event: " + event));
    }

    private Flux<MessageDTO<Float>> currentPrice(Flux<Map<String, Object>> input) {
        return null;
    }

    private Flux<Map<String, Object>> selectOnlyPriceUpdateEvents(Flux<Map<String, Object>> input) {
        return null;
    }

    @Override
    public Flux<MessageDTO<Float>> pricesStream(Flux<Long> intervalPreferencesStream) {
        return null;
    }

    public Flux<MessageDTO<Float>> getSharedStream() {
        return sharedStream;
    }
    
}
