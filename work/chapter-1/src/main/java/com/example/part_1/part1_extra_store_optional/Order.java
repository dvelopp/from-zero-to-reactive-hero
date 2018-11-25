package com.example.part_1.part1_extra_store_optional;

import rx.Observable;

import java.util.Objects;

import com.example.annotations.Complexity;
import com.example.annotations.Optional;

import static com.example.annotations.Complexity.Level.MEDIUM;

public class Order {
    private final String id;
    private final String userId;
    private final Iterable<String> productsIds;

    public Order(String id, String userId, Iterable<String> productsIds) {
        this.id = Objects.requireNonNull(id);
        this.userId = Objects.requireNonNull(userId);
        this.productsIds = Objects.requireNonNull(productsIds);
    }

    @Optional
    @Complexity(MEDIUM)
    public Observable<Long> getTotalPrice() {
        return Observable.from(productsIds)
                .map(ProductsCatalog::findById)
                .reduce(0l, (val, product) -> val + product.getPrice());
    }

    public String getId() {
        return this.id;
    }

    public String getUserId() {
        return this.userId;
    }

    public Iterable<String> getProductsIds() {
        return this.productsIds;
    }
}
