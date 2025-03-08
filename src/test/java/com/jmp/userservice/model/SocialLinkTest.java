package com.jmp.userservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SocialLinkTest {

    @Test
    void createModel_WhenInvalidLinkType_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new SocialLink("invalid-link-type", "https://www.pornhub.com")
        );
        assertEquals("Social link type not allowed: invalid-link-type", exception.getMessage());
    }

    @Test
    void createModel_WhenInvalidLinkUrl_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new SocialLink("telegram", "invalid-link-url")
        );
        assertEquals("Social link url is invalid: invalid-link-url", exception.getMessage());
    }

    @Test
    void createModel_WhenValidLinkUrlAndLinkType_ShouldReturnObject() {
        SocialLink socialLink = new SocialLink("telegram", "https://www.pornhub.com");
        assertEquals("telegram", socialLink.getType());
        assertEquals("https://www.pornhub.com", socialLink.getUrl());
    }
}
