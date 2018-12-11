package com.example.part_11.service.utils;

import java.util.Map;

public interface MessageUnpacker {
    boolean supports(String messageType);

    Map<String, Object> unpack(String message);
}
