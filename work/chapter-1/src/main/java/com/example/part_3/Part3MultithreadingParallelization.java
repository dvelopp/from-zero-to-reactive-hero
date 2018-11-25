package com.example.part_3;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

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
        return streamOfLongRunningSources.flatMap(Mono::fromCallable).subscribeOn(Schedulers.elastic());
    }

}
