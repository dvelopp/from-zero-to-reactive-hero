package com.example.part_4;

import reactor.core.publisher.Flux;

import java.util.concurrent.CountDownLatch;

import org.reactivestreams.Publisher;

import com.example.annotations.Complexity;
import com.example.annotations.Optional;
import com.example.common.StringEventPublisher;

import static com.example.annotations.Complexity.Level.MEDIUM;

public class Part4ExtraBackpressure_Optional {

    @Complexity(MEDIUM)
    public static Publisher<String> handleBackpressureWithBuffering(StringEventPublisher stringEventPublisher) {
        return Flux.<String>create(sink -> stringEventPublisher.registerEventListener(sink::next))
                .buffer(1)
                .flatMap(Flux::fromIterable);
    }

    @Optional
    @Complexity(MEDIUM)
    public static void dynamicDemand(Flux<String> source, CountDownLatch countDownOnComplete) {
        // TODO: provide own implementation of Subscriber. Manage request count manually.
        // TODO: two times increase request size each time when previous demand has been satisfied

        // HINT: Consider usage of reactor.core.publisher.BaseSubscriber
        // HINT: count down onComplete



    }
}
