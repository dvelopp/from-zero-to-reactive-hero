package com.example.homework;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.test.StepVerifier;

import java.math.BigInteger;
import java.time.Duration;
import java.util.function.Consumer;

import org.junit.Test;

import com.example.annotations.Complexity;

import static com.example.annotations.Complexity.Level.HARD;
import static com.example.annotations.Complexity.Level.MEDIUM;

public class Homework {

    @Complexity(HARD)
    public static Flux<BigInteger> generate(Long limit) {
        Consumer<FluxSink<BigInteger>> fluxSinkConsumer = fluxSink -> {
            for (long i = 0; i < ((limit == null) ? Long.MAX_VALUE : limit); i++) {
                if (isPrime(i)) {
                    fluxSink.next(BigInteger.valueOf(i));
                }
            }
        };
        return Flux.create(fluxSinkConsumer);
    }

    public static boolean isPrime(long n) {
        for (int i = 2; 2 * i < n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    @Test
    @Complexity(MEDIUM)
    public void testGeneration() {
        Flux<BigInteger> generate = Homework.generate(10000l);
        StepVerifier.create(generate)
                .expectSubscription()
                .expectNextMatches(bigInteger -> isPrime(bigInteger.longValue()))
                .thenCancel()
                .verify(Duration.ofSeconds(1));
    }

}
