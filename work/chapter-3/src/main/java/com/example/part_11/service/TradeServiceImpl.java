/*
 * Copyright (c) 2018 Nextiva, Inc. to Present.
 * All rights reserved.
 */
package com.example.part_11.service;

import reactor.core.publisher.Flux;

import org.springframework.stereotype.Component;

import com.example.part_11.dto.MessageDTO;

@Component
public class TradeServiceImpl implements TradeService {

    @Override
    public Flux<MessageDTO<MessageDTO.Trade>> tradesStream() {
        return null;
    }

}
