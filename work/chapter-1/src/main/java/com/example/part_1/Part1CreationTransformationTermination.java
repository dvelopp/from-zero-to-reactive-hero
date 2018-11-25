package com.example.part_1;

import reactor.util.annotation.Nullable;
import rx.Observable;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.annotations.Complexity;

import static com.example.annotations.Complexity.Level.EASY;
import static com.example.annotations.Complexity.Level.MEDIUM;

public class Part1CreationTransformationTermination {

    @Complexity(EASY)
    public static Observable<String> justABC() {
        return Observable.just("ABC");
    }

    @Complexity(EASY)
    public static Observable<String> fromArray(String... args) {
        return Observable.from(args);
    }

    @Complexity(EASY)
    public static Observable<String> error(Throwable t) {
        return Observable.error(t);
    }

    @Complexity(EASY)
    public static Observable<Integer> convertNullableValueToObservable(@Nullable Integer nullableElement) {
        return Optional.ofNullable(nullableElement).map(Observable::just).orElseGet(Observable::empty);
    }

    @Complexity(EASY)
    public static Observable<String> deferCalculation(Func0<Observable<String>> calculation) {
        return Observable.defer(calculation);
    }

    @Complexity(EASY)
    public static Observable<Long> interval(long interval, TimeUnit timeUnit) {
        return Observable.interval(interval, timeUnit);
    }

    @Complexity(EASY)
    public static Observable<String> mapToString(Observable<Long> input) {
        return Observable.just(input).flatMap(longObservable -> longObservable.map(String::valueOf));
    }

    @Complexity(EASY)
    public static Observable<String> findAllWordsWithPrefixABC(Observable<String> input) {
        return Observable.just(input).flatMap(o -> o.filter(s -> s.startsWith("ABC")));
    }

    @Complexity(MEDIUM)
    public static Observable<String> fromFutureInIOScheduler(Future<String> future) {
        return Observable.from(future, Schedulers.io());
    }

    @Complexity(MEDIUM)
    public static void iterateNTimes(int times, AtomicInteger counter) {
        Observable.range(1, times).subscribe(integer -> counter.incrementAndGet());
    }

    @Complexity(MEDIUM)
    public static Observable<Character> flatMapWordsToCharacters(Observable<String> input) {
        return input.map(s -> s.split("")).map(Observable::from).flatMap(s -> s.map(o -> o.charAt(0)));
    }
}
