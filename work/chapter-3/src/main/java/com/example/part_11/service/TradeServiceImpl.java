/*
 * Copyright (c) 2018 Nextiva, Inc. to Present.
 * All rights reserved.
 */
package com.example.part_11.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.part_11.domain.Trade;
import com.example.part_11.dto.MessageDTO;
import com.example.part_11.utils.MessageMapper;

@Component
public class TradeServiceImpl implements TradeService {

    @Autowired
    private CryptoService cryptoService;
    @Autowired
    private MessageMapper messageMapper;

    private static final Logger logger = Logger.getLogger("trade-service");

    private Flux<MessageDTO<MessageDTO.Trade>> sharedStream;

    @PostConstruct
    public void init() {
        sharedStream = cryptoService.eventsStream()
                .transform(this::filterAndMapTradingEvents)
                .transform(trades -> Flux.merge(
                        trades,
                        trades.transform(this::mapToDomainTrade)
                                .then(Mono.empty())
                ));
    }

    @Override
    public Flux<MessageDTO<MessageDTO.Trade>> tradesStream() {
        return sharedStream;
    }

    Flux<MessageDTO<MessageDTO.Trade>> filterAndMapTradingEvents(Flux<Map<String, Object>> input) {
        return input
                .filter(messageMapper::isTradeMessageType)
                .map(messageMapper::mapToTradeMessage);
    }

    Flux<Trade> mapToDomainTrade(Flux<MessageDTO<MessageDTO.Trade>> input) {
        return input.map(messageMapper::mapToDomain);
    }

}
