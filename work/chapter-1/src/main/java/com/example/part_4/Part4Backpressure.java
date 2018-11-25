package com.example.part_4;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

import com.example.annotations.Complexity;

import static com.example.annotations.Complexity.Level.EASY;
import static com.example.annotations.Complexity.Level.MEDIUM;

public class Part4Backpressure {

    @Complexity(EASY)
    public static Flux<String> dropElementsOnBackpressure(Flux<String> upstream) {
        return upstream.onBackpressureDrop();

    }

    @Complexity(MEDIUM)
    public static Flux<List<Long>> backpressureByBatching(Flux<Long> upstream) {
        return upstream.window(Duration.ofMillis(1000)).flatMap(Flux::collectList);
    }

}
