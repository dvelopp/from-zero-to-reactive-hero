package com.example.part_5;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

import org.reactivestreams.Publisher;

import com.example.annotations.Complexity;

import static com.example.annotations.Complexity.Level.EASY;
import static com.example.annotations.Complexity.Level.HARD;
import static com.example.annotations.Complexity.Level.MEDIUM;

public class Part5ResilienceResponsive {

    @Complexity(EASY)
    public static Publisher<String> fallbackHelloOnEmpty(Flux<String> emptyPublisher) {
        return emptyPublisher.defaultIfEmpty("Hello");
    }

    @Complexity(EASY)
    public static Publisher<String> fallbackHelloOnError(Flux<String> failurePublisher) {
        return failurePublisher.onErrorReturn("Hello");
    }

    @Complexity(EASY)
    public static Publisher<String> retryOnError(Mono<String> failurePublisher) {
        return failurePublisher.retry();
    }

    @Complexity(MEDIUM)
    public static Publisher<String> timeoutLongOperation(CompletableFuture<String> longRunningCall) {
        return Mono.fromFuture(longRunningCall).timeout(Duration.ofSeconds(1), Mono.just("Hello"));
    }

    @Complexity(HARD)
    public static Publisher<String> timeoutLongOperation(Callable<String> longRunningCall) {
        return Mono.fromCallable(longRunningCall).subscribeOn(Schedulers.single()).timeout(Duration.ofSeconds(1), Mono.just("Hello"));
    }
}
