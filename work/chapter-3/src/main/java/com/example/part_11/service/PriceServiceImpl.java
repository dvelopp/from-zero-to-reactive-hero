/*
 * Copyright (c) 2018 Nextiva, Inc. to Present.
 * All rights reserved.
 */
package com.example.part_11.service;

import reactor.core.publisher.Flux;

import org.springframework.stereotype.Service;

import com.example.part_11.dto.MessageDTO;

/**
 * Class Description goes here.
 * Created by dmitrypashkov on 12/1/18
 */
@Service
public class PriceServiceImpl implements PriceService {
    @Override
    public Flux<MessageDTO<Float>> pricesStream(Flux<Long> intervalPreferencesStream) {
        return null;
    }
}
