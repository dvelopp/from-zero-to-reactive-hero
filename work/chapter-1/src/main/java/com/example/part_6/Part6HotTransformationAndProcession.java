package com.example.part_6;

import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.TopicProcessor;

import org.reactivestreams.Publisher;

import com.example.annotations.Complexity;

import static com.example.annotations.Complexity.Level.MEDIUM;

public class Part6HotTransformationAndProcession {

    @Complexity(MEDIUM)
    public static Publisher<String> transformToHot(Flux<String> coldSource) {
        return coldSource.publish().autoConnect();
    }

    @Complexity(MEDIUM)
    public static Publisher<String> replayLast3ElementsInHotFashion(Flux<String> coldSource) {
        return coldSource.replay(3).autoConnect();
    }


    @Complexity(MEDIUM)
    public static Publisher<String> transformToHotUsingProcessor(Flux<String> coldSource) {
        return coldSource.subscribeWith(DirectProcessor.create());
    }

    @Complexity(MEDIUM)
    public static Flux<String> processEachSubscriberOnSeparateThread(Flux<String> coldSource) {
        return coldSource.subscribeWith(TopicProcessor.create());
    }
}
