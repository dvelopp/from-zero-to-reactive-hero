package com.example.part_5;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import org.reactivestreams.Publisher;

import com.example.annotations.Complexity;

import static com.example.annotations.Complexity.Level.HARD;

public class Part5ResilienceResponsive_Optional {

    @Complexity(HARD)
    public static Publisher<Integer> provideSupportOfContinuation(Flux<Integer> values) {
        return values.onErrorContinue((throwable, o) -> {
        });
    }

    @Complexity(HARD)
    public static Publisher<Integer> provideSupportOfContinuationWithoutErrorStrategy(Flux<Integer> values, Function<Integer, Integer> mapping) {
        return values.concatMap(e -> Mono.fromSupplier(() -> mapping.apply(e)).onErrorResume(t -> Mono.empty()));
    }
}
