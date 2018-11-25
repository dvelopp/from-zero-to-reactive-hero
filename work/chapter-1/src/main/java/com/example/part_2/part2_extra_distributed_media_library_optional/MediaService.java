package com.example.part_2.part2_extra_distributed_media_library_optional;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import com.example.annotations.Complexity;
import com.example.annotations.Optional;

import static com.example.annotations.Complexity.Level.MEDIUM;

public class MediaService {

    private final List<Server> availableContentServers = Servers.list();

    @Optional
    @Complexity(MEDIUM)
    public Mono<Video> findVideo(String videoName) {
        return Mono.first(Flux.fromIterable(availableContentServers)
                .map(server -> server.searchOne(videoName)).toStream().collect(Collectors.toList()));
    }
}
