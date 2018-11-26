package com.example.part_7;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import org.reactivestreams.Publisher;

import com.example.annotations.Complexity;

import static com.example.annotations.Complexity.Level.EASY;

public class Part7Context {

    @Complexity(EASY)
    public static Mono<String> grabDataFromTheGivenContext(Object key) {
        return Mono.subscriberContext()
                .filter(context -> context.hasKey(key))
                .flatMap(context -> Mono.just(context.get(key)));
    }

    @Complexity(EASY)
    public static Mono<String> provideCorrectContext(Mono<String> source, Object key, Object value) {
        return source.subscriberContext(context -> context.put(key, value));
    }

    @Complexity(EASY)
    public static Flux<String> provideCorrectContext(
            Publisher<String> sourceA, Context contextA,
            Publisher<String> sourceB, Context contextB) {
        return Flux.from(sourceA).subscriberContext(contextA)
                .mergeWith(Flux.from(sourceB))
                .subscriberContext(contextB);

    }
}
