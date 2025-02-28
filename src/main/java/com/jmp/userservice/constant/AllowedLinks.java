package com.jmp.userservice.constant;



import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum AllowedLinks {
    TELEGRAM("telegram"),
    VK("vk");

    private final String key;

    AllowedLinks(String key) {
        this.key = key;
    }

    public static Set<String> getAllowedLinks() {
            return Arrays.stream(AllowedLinks.values())
                    .map(AllowedLinks::getKey)
                    .collect(Collectors.toSet());
        }
}
