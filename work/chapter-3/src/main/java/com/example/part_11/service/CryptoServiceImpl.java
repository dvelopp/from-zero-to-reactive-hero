/*
 * Copyright (c) 2018 Nextiva, Inc. to Present.
 * All rights reserved.
 */
package com.example.part_11.service;

import reactor.core.publisher.Flux;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class CryptoServiceImpl implements CryptoService {

    @Override
    public Flux<Map<String, Object>> eventsStream() {
        return null;
    }

}
