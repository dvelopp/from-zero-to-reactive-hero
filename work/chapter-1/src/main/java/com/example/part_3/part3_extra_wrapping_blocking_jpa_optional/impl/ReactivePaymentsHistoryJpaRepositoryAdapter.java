package com.example.part_3.part3_extra_wrapping_blocking_jpa_optional.impl;

import reactor.core.publisher.Flux;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.annotations.Complexity;
import com.example.annotations.Optional;
import com.example.part_3.part3_extra_wrapping_blocking_jpa_optional.ConnectionsPool;
import com.example.part_3.part3_extra_wrapping_blocking_jpa_optional.Payment;
import com.example.part_3.part3_extra_wrapping_blocking_jpa_optional.PaymentsHistoryJpaRepository;
import com.example.part_3.part3_extra_wrapping_blocking_jpa_optional.PaymentsHistoryReactiveJpaRepository;

import static com.example.annotations.Complexity.Level.HARD;
import static reactor.core.publisher.Flux.defer;
import static reactor.core.publisher.Flux.fromIterable;
import static reactor.core.scheduler.Schedulers.fromExecutorService;

public class ReactivePaymentsHistoryJpaRepositoryAdapter
        implements PaymentsHistoryReactiveJpaRepository {

    public static final ExecutorService ES = Executors.newFixedThreadPool(ConnectionsPool.instance().size());
    private PaymentsHistoryJpaRepository repository = new BlockingPaymentsHistoryJpaRepository();

    @Optional
    @Complexity(HARD)
    public Flux<Payment> findAllByUserId(String userId) {
        return defer(
                () -> fromIterable(repository.findAllByUserId(userId))
        ).subscribeOn(fromExecutorService(ES));
    }
}
