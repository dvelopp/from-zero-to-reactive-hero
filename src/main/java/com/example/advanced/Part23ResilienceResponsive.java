package com.example.advanced;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public class Part23ResilienceResponsive {

    public static Publisher<String> retryOnError(Mono<String> failurePublisher) {
        // TODO: retry operation if error
        // HINT: Flux#retry()
        throw new RuntimeException("Not implemented yet");
    }

    public static Publisher<String> timeoutLongOperation(CompletableFuture<String> longRunningCall) {
        // TODO: limit the overall operation execution to one second
        // TODO: in case of timeout return fallback with "Hello"
        // HINT: Mono.fromFuture() + Mono#timeout(Duration, Mono)
        throw new RuntimeException("Not implemented yet");
    }

    public static Publisher<String> timeoutLongOperation(Callable<String> longRunningCall) {
        // TODO: limit the overall operation execution to one second
        // TODO: in case of timeout return fallback with "Hello"
        // HINT: bear in mind that execution should occur on different thread
        // HINT: Mono.fromCallable + .subscribeOn + Mono#timeout(Duration, Mono)
        throw new RuntimeException("Not implemented yet");
    }

    public static Publisher<String> fallbackHelloOnError(Flux<String> failurePublisher) {
        // TODO: return fallback on error
        // TODO: in case of error return fallback with "Hello"
        // HINT: Flux#onErrorResume or Flux#onErrorReturn
        throw new RuntimeException("Not implemented yet");
    }

    public static Publisher<String> fallbackHelloOnEmpty(Flux<String> emptyPublisher) {
        // TODO: return fallback on empty source
        // TODO: in case of no value emitted return fallback with "Hello"
        // HINT: Flux#switchIfEmpty() or Flux#defaultIfEmpty
        throw new RuntimeException("Not implemented yet");
    }
}
