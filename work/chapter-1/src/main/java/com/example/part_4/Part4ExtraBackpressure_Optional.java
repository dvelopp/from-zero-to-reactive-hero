package com.example.part_4;

import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

import java.util.concurrent.CountDownLatch;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import com.example.annotations.Complexity;
import com.example.annotations.Optional;
import com.example.common.StringEventPublisher;

import static com.example.annotations.Complexity.Level.MEDIUM;

public class Part4ExtraBackpressure_Optional {

    public static void main(String[] args) {
        Flux<Object> lol = Flux.create(fluxSink -> {
            fluxSink.next("LOL");
        });
        lol.subscribe(System.out::println);
    }

    @Complexity(MEDIUM)
    public static Publisher<String> handleBackpressureWithBuffering(StringEventPublisher stringEventPublisher) {
        return Flux.<String>create(sink -> stringEventPublisher.registerEventListener(sink::next))
                .buffer(1)
                .flatMap(Flux::fromIterable);
    }

    @Optional
    @Complexity(MEDIUM)
    public static void dynamicDemand(Flux<String> source, CountDownLatch countDownOnComplete) {
        source.subscribe(new BaseSubscriber<String>() {
            int windowSize = 1;
            int receivedSize = 0;

            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                subscription.request(windowSize);
            }

            @Override
            protected void hookOnNext(String value) {
                receivedSize++;
                if (receivedSize == windowSize) {
                    windowSize *= 2;
                    receivedSize = 0;
                    upstream().request(windowSize);
                }
            }

            @Override
            protected void hookFinally(SignalType type) {
                countDownOnComplete.countDown();
            }
        });

    }
}
