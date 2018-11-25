package com.example.part_2;

import reactor.core.publisher.Flux;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.reactivestreams.Publisher;

import com.example.annotations.Complexity;
import com.example.annotations.Optional;

import static com.example.annotations.Complexity.Level.EASY;
import static com.example.annotations.Complexity.Level.HARD;

public class Part2ExtraExercises_Optional {

    @Optional
    @Complexity(EASY)
    public static String firstElementFromSource(Flux<String> source) {
        return source.blockFirst();
    }

    @Optional
    @Complexity(EASY)
    public static Publisher<String> mergeSeveralSourcesOrdered(Publisher<String>... sources) {
        return Flux.mergeSequential(sources);
    }

    @Optional
    @Complexity(EASY)
    public static Publisher<String> concatSeveralSourcesOrdered(Publisher<String>... sources) {
        return Flux.concat(sources);
    }

    //TODO 
    @Optional
    @Complexity(HARD)
    public static Publisher<String> readFile(String filename) {
        return Flux.using(() -> new File(filename).toPath(), path -> {
            try {
                return Flux.fromStream(Files.lines(path));
            } catch (IOException e) {
                return Flux.empty();
            }
        }, path -> {
            //releasing resource if necessary
        });
    }
}
