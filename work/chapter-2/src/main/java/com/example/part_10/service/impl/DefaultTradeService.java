package com.example.part_10.service.impl;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.logging.Logger;

import com.example.part_10.domain.Trade;
import com.example.part_10.dto.MessageDTO;
import com.example.part_10.repository.TradeRepository;
import com.example.part_10.service.CryptoService;
import com.example.part_10.service.TradeService;
import com.example.part_10.service.utils.MessageMapper;

public class DefaultTradeService implements TradeService {

    private final Flux<MessageDTO<MessageDTO.Trade>> sharedStream;

    public DefaultTradeService(CryptoService service, TradeRepository repository) {
        sharedStream = service.eventsStream()
                .transform(this::filterAndMapTradingEvents)
                .transform(trades -> Flux.merge(
                        trades,
                        trades.transform(this::mapToDomainTrade)
                                .transform(repository::saveAll)
                                .then(Mono.empty())
                ));
    }

    @Override
    public Flux<MessageDTO<MessageDTO.Trade>> tradesStream() {
        return sharedStream;
    }

    Flux<MessageDTO<MessageDTO.Trade>> filterAndMapTradingEvents(Flux<Map<String, Object>> input) {
        return input
                .filter(MessageMapper::isTradeMessageType)
                .map(MessageMapper::mapToTradeMessage);
    }

    Flux<Trade> mapToDomainTrade(Flux<MessageDTO<MessageDTO.Trade>> input) {
        return input.map(MessageMapper::mapToDomain);
    }

}
