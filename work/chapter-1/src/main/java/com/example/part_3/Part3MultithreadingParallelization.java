package com.example.part_3;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.concurrent.Callable;

import org.reactivestreams.Publisher;

import com.example.annotations.Complexity;

import static com.example.annotations.Complexity.Level.EASY;
import static com.example.annotations.Complexity.Level.HARD;

public class Part3MultithreadingParallelization {

    @Complexity(EASY)
    public static Publisher<String> publishOnParallelThreadScheduler(Flux<String> source) {
        return source.publishOn(Schedulers.parallel());
    }

    @Complexity(EASY)
    public static Publisher<String> subscribeOnSingleThreadScheduler(Callable<String> blockingCall) {
        return Mono.fromCallable(blockingCall).subscribeOn(Schedulers.single());
    }

    @Complexity(EASY)
    public static ParallelFlux<String> paralellizeWorkOnDifferentThreads(Flux<String> source) {
        return source.parallel().runOn(Schedulers.parallel());
    }

    @Complexity(HARD)
    public static Publisher<String> paralellizeLongRunningWorkOnUnboundedAmountOfThread(Flux<Callable<String>> streamOfLongRunningSources) {
        return streamOfLongRunningSources.parallel().runOn(Schedulers.elastic()).flatMap(Mono::fromCallable);
    }

    public static void main(String[] args) throws InterruptedException {
        Flux<Integer> fluxWithDelay = Flux.range(1, 100).delaySubscription(Duration.ofMillis(1000));
        Flux<Integer> fluxWithoutDelay = Flux.range(1, 100);
        Flux<Tuple2<Integer, Integer>> tuple2Flux = fluxWithDelay.zipWith(fluxWithoutDelay);
        tuple2Flux.subscribe(System.out::println);

        Thread.sleep(10000);
    }

}
