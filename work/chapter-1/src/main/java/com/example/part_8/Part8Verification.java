package com.example.part_8;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Supplier;

import org.junit.Test;

import com.example.annotations.Complexity;

import static com.example.annotations.Complexity.Level.HARD;
import static com.example.annotations.Complexity.Level.MEDIUM;

public class Part8Verification {

    @Complexity(MEDIUM)
    @Test
    public void verifyThen10ElementsEmitted() {
        Flux<Integer> toVerify = Flux.fromStream(new Random().ints().boxed())
                .take(15)
                .skip(5);

        StepVerifier.create(toVerify)
                .expectSubscription()
                .expectNextCount(10)
                .verifyComplete();

    }

    @Complexity(HARD)
    @Test
    public void verifyEmissionWithVirtualTimeScheduler() {
        Supplier<Flux<Long>> toVerify = () -> Flux.interval(Duration.ofDays(1))
                .take(15)
                .skip(5);

        StepVerifier.withVirtualTime(toVerify)
                .thenAwait(Duration.ofDays(15))
                .recordWith(ArrayList::new)
                .expectNextCount(10)
                .expectRecordedMatches(c -> c.size() == 10)
                .verifyComplete();
    }

}
