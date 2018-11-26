package com.example.part_10.service.impl;

import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;

import java.time.Duration;
import java.util.Map;
import java.util.logging.Logger;

import com.example.part_10.dto.MessageDTO;
import com.example.part_10.service.CryptoService;
import com.example.part_10.service.PriceService;
import com.example.part_10.service.utils.MessageMapper;
import com.example.part_10.service.utils.Sum;

public class DefaultPriceService implements PriceService {

    private static final Logger logger = Logger.getLogger("price-service");

    private static final long DEFAULT_AVG_PRICE_INTERVAL = 30L;

    private final Flux<MessageDTO<Float>> sharedStream;

    public DefaultPriceService(CryptoService cryptoService) {
        sharedStream = cryptoService.eventsStream()
                .doOnNext(event -> logger.fine("Incoming event: " + event))
                .transform(this::selectOnlyPriceUpdateEvents)
                .transform(this::currentPrice)
                .doOnNext(event -> logger.fine("Price event: " + event));
    }


    public Flux<MessageDTO<Float>> pricesStream(Flux<Long> intervalPreferencesStream) {
        return sharedStream.transform(mainFlow -> Flux.merge(
                mainFlow,
                averagePrice(intervalPreferencesStream, mainFlow)
        ));
    }

    Flux<Map<String, Object>> selectOnlyPriceUpdateEvents(Flux<Map<String, Object>> input) {
        return input
                .filter(MessageMapper::isPriceMessageType)
                .filter(MessageMapper::isValidPriceMessage);

    }

    Flux<MessageDTO<Float>> currentPrice(Flux<Map<String, Object>> input) {
        return input
                .map(MessageMapper::mapToPriceMessage);

    }

    Flux<MessageDTO<Float>> averagePrice(
            Flux<Long> requestedInterval,
            Flux<MessageDTO<Float>> priceData) {
        return requestedInterval.startWith(DEFAULT_AVG_PRICE_INTERVAL)
                .switchMap(timeInterval -> priceData.window(Duration.ofSeconds(timeInterval)))
                .flatMap(flux -> {
                            Flux<GroupedFlux<String, MessageDTO<Float>>> groupedFluxFlux = flux
                                    .groupBy(MessageDTO::getCurrency);
                            return groupedFluxFlux
                                    .flatMap(keyFlux -> keyFlux
                                            .map(MessageDTO::getData)
                                            .reduce(Sum.empty(), Sum::add)
                                            .map(Sum::avg)
                                            .map(avg -> MessageDTO.avg(avg, keyFlux.key(), "Local Market")));
                        }
                );

       /* return requestedInterval
                .switchMap(val -> {
                    if (val == null) {
                        return priceData.window(Duration.ofMillis(DEFAULT_AVG_PRICE_INTERVAL));
                    } else {
                        return priceData.window(Duration.ofMillis(val));
                    }
                })
                .flatMap(flux -> flux)
                .groupBy(MessageDTO::getCurrency)
                .flatMap(flux -> {
                    Mono<Sum> reduce = flux.reduce(Sum.empty(), (sum, messageDTO) -> sum.add(messageDTO.getData()));
                    MessageDTO<Float> mes = MessageDTO.price(reduce.block().avg(), flux.blockFirst().getCurrency(), flux.blockFirst().getMarket());
                    return Flux.just(mes);
                });*/
    }


}
