package com.example.part_1.part1_extra_store_optional;


import rx.Observable;

import com.example.annotations.Complexity;
import com.example.annotations.Optional;

import static com.example.annotations.Complexity.Level.MEDIUM;

public class UserActivityUtils {

    @Optional
    @Complexity(MEDIUM)
    public static Observable<Product> findMostExpansivePurchase(Observable<Order> ordersHistory) {
        return ordersHistory
                .flatMap(order -> Observable.from(order.getProductsIds()))
                .map(ProductsCatalog::findById)
                .reduce(null, (p1, p2) -> p1 != null && p1.getPrice() > p2.getPrice() ? p1 : p2);
    }
}
