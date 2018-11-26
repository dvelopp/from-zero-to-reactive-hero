package com.example.part_9;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

import org.junit.Test;

import com.example.annotations.Complexity;

import static com.example.annotations.Complexity.Level.MEDIUM;

public class Part9Debugging {

    @Test
    @Complexity(MEDIUM)
    public void checkMath() {
        Hooks.onOperatorDebug();
        StepVerifier.create(Flux.range(0, 1234567)
                .transform(ReactiveMath::doCalculation)
                .take(1))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();

    }
}
