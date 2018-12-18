package com.example.part_11.controller.ws;

import java.util.Collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyMap;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class WebSocketMessageMapper {

	private final Jackson2JsonEncoder encoder;

	public WebSocketMessageMapper(ObjectMapper mapper) {
		encoder = new Jackson2JsonEncoder(mapper);
	}

	public Flux<DataBuffer> encode(Flux<?> outbound, DataBufferFactory dataBufferFactory) {
		return outbound
				.flatMap(i -> encoder.encode(
						Mono.just(i),
						dataBufferFactory,
						ResolvableType.forType(Object.class),
						APPLICATION_JSON,
						emptyMap()
				));

	}
}