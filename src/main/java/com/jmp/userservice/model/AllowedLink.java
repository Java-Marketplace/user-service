package com.jmp.userservice.model;


import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum AllowedLink {
    TELEGRAM("telegram"),
    VK("vk");

    private final String key;

    AllowedLink(String key) {
        this.key = key;
    }

    public static Set<String> getAllowedLinks() {
        return Arrays.stream(AllowedLink.values())
                .map(AllowedLink::getKey)
                .collect(Collectors.toSet());
    }
}
