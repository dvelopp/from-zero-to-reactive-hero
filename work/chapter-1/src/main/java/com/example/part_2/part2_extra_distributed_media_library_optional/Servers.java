package com.example.part_2.part2_extra_distributed_media_library_optional;

import java.util.List;

import static java.util.Arrays.asList;

public class Servers {

    private final static List<Server> servers = asList(
            new Server("http://a.servers.com"),
            new Server("http://b.servers.com"),
            new Server("http://c.servers.com"),
            new Server("http://d.servers.com"),
            new Server("http://e.servers.com"),
            new Server("http://f.servers.com")
    );

    public static List<Server> list() {
        return servers;
    }

}
