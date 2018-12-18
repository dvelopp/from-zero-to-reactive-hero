package com.example.part_11.controller.ws;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import com.example.part_11.dto.MessageDTO;
import com.example.part_11.service.PriceService;
import com.example.part_11.service.TradeService;

@Service
public class WSHandler implements WebSocketHandler {

    private static final Logger logger = Logger.getLogger("WSHandler");
    private static final String WRONG_NUMBER_MSG = "Wrong number";

    @Autowired
    private WebSocketMessageMapper mapper;
    @Autowired
    private PriceService priceService;
    @Autowired
    private TradeService tradeService;

    public WSHandler(WebSocketMessageMapper mapper,
                     PriceService priceService,
                     TradeService tradeService) {
        this.mapper = mapper;
        this.priceService = priceService;
        this.tradeService = tradeService;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .transform(this::handleRequestedAveragePriceIntervalValue)
                .publishOn(Schedulers.parallel())
                .transform(this::handle)
                .onBackpressureBuffer()
                .transform(m -> mapper.encode(m, session.bufferFactory()))
                .map(db -> new WebSocketMessage(WebSocketMessage.Type.TEXT, db))
                .as(session::send);
    }

    public Flux<MessageDTO> handle(Flux<Long> input) {
        return Flux.merge(priceService.pricesStream(input), tradeService.tradesStream());
    }

    private Flux<Long> handleRequestedAveragePriceIntervalValue(Flux<String> requestedInterval) {
        return requestedInterval
                .map(Long::valueOf)
                .filter(o -> o > 0 && o < 60)
                .onErrorContinue((throwable, o) -> logger.log(Level.WARNING, throwable, () -> WRONG_NUMBER_MSG));
    }

}

