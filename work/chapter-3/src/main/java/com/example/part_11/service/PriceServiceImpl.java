/*
 * Copyright (c) 2018 Nextiva, Inc. to Present.
 * All rights reserved.
 */
package com.example.part_11.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;

import java.time.Duration;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.part_11.dto.MessageDTO;
import com.example.part_11.utils.MessageMapper;
import com.example.part_11.utils.Sum;

@Service
public class PriceServiceImpl implements PriceService {

    private static final Logger logger = Logger.getLogger("price-service");
    private static final long DEFAULT_AVG_PRICE_INTERVAL = 30L;

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
        return input.map(MessageMapper::mapToPriceMessage);
    }

    private Flux<Map<String, Object>> selectOnlyPriceUpdateEvents(Flux<Map<String, Object>> input) {
        return input
                .filter(MessageMapper::isPriceMessageType)
                .filter(MessageMapper::isValidPriceMessage);
    }

    @Override
    public Flux<MessageDTO<Float>> pricesStream(Flux<Long> intervalPreferencesStream) {
        return sharedStream.transform(mainFlow -> Flux.merge(
                mainFlow,
                averagePrice(intervalPreferencesStream, mainFlow)
        ));
    }

    public Flux<MessageDTO<Float>> getSharedStream() {
        return sharedStream;
    }

    Flux<MessageDTO<Float>> averagePrice(
            Flux<Long> requestedInterval,
            Flux<MessageDTO<Float>> priceData) {
        return requestedInterval.startWith(DEFAULT_AVG_PRICE_INTERVAL)
                .switchMap(timeInterval -> priceData.window(Duration.ofSeconds(timeInterval)))
                .flatMap(flux -> {
                            Flux<GroupedFlux<String, MessageDTO<Float>>> groupedFluxFlux = flux
                                    .groupBy(MessageDTO::getCurrency);
                            return groupedFluxFlux
                                    .flatMap(keyFlux -> keyFlux
                                            .map(MessageDTO::getData)
                                            .reduce(Sum.empty(), Sum::add)
                                            .map(Sum::avg)
                                            .map(avg -> MessageDTO.avg(avg, keyFlux.key(), "Local Market")));
                        }
                );

    }

}
