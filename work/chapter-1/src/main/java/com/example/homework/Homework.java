package com.example.homework;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigInteger;

import org.junit.Test;

import com.example.annotations.Complexity;

import static com.example.annotations.Complexity.Level.HARD;
import static com.example.annotations.Complexity.Level.MEDIUM;

public class Homework {

    @Complexity(HARD)
    public static Flux<BigInteger> generate() {
        return Flux.create(fluxSink -> {
            for (long i = 1; i < Long.MAX_VALUE && !fluxSink.isCancelled(); i++) {
                BigInteger val = BigInteger.valueOf(i);
                if (val.isProbablePrime(1)) {
                    fluxSink.next(val);
                }
            }
        });
    }

    @Test
    @Complexity(MEDIUM)
    public void testGeneration() {
        StepVerifier.create(Homework.generate().doOnNext(System.out::println))
                .expectSubscription()
                .thenRequest(10)
                .expectNextMatches(bigInteger -> bigInteger.isProbablePrime(1))
                .thenCancel()
                .verify();
    }

}
