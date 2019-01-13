package com.example.part_2;

import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.stream.Collectors;

import org.reactivestreams.Publisher;

import com.example.annotations.Complexity;

import static com.example.annotations.Complexity.Level.EASY;
import static com.example.annotations.Complexity.Level.HARD;
import static com.example.annotations.Complexity.Level.MEDIUM;

public class Part2CreationTransformationTermination {

    @Complexity(EASY)
    public static Mono<List<String>> collectAllItemsToList(Flux<String> source) {
        return source.collect(Collectors.toList());
    }

    @Complexity(EASY)
    public static String lastElementFromSource(Flux<String> source) {
        return source.blockLast();
    }

    @Complexity(EASY)
    public static Publisher<String> mergeSeveralSources(Publisher<String>... sources) {
        return Flux.merge(sources);
    }

    @Complexity(EASY)
    public static Publisher<String> fromFirstEmitted(Publisher<String>... sources) {
        return Flux.first(sources);
    }

    @Complexity(EASY)
    public static Publisher<GroupedFlux<Character, String>> groupWordsByFirstLatter(Flux<String> words) {
        return words.groupBy(s -> s.charAt(0));
    }

    @Complexity(MEDIUM)
    public static Mono<String> executeLazyTerminationOperationAndSendHello(Flux<String> source) {
        return source.then(Mono.just("Hello"));
    }

    @Complexity(MEDIUM)
    public static Publisher<String> zipSeveralSources(Publisher<String> prefix,
                                                      Publisher<String> word,
                                                      Publisher<String> suffix) {
        return Flux.zip(prefix, word, suffix).map(objects -> objects.getT1() + objects.getT2() + objects.getT3());
    }

    @Complexity(HARD)
    public static Publisher<String> combineSeveralSources(Publisher<String> prefix,
                                                          Publisher<String> word,
                                                          Publisher<String> suffix) {
        return Flux.combineLatest(prefix, word, suffix, Tuples::fromArray)
                .cast(Tuple3.class)
                .map(tuple3 -> (String) tuple3.getT1() + tuple3.getT2() + tuple3.getT3());
    }

    @Complexity(HARD)
    public static Flux<IceCreamBall> fillIceCreamWaffleBowl(
            Flux<IceCreamType> clientPreferences,
            Flux<IceCreamBall> vanillaIceCreamStream,
            Flux<IceCreamBall> chocolateIceCreamStream) {
        return clientPreferences.switchMap(iceCreamType -> {
            if (iceCreamType == IceCreamType.VANILLA) {
                return vanillaIceCreamStream;
            } else if (iceCreamType == IceCreamType.CHOCOLATE) {
                return chocolateIceCreamStream;
            } else {
                throw new IllegalArgumentException();
            }
        });
    }

}
