package com.jmp.userservice.unit.validation;

import com.jmp.userservice.model.SocialLink;
import com.jmp.userservice.support.BaseUnitTest;
import com.jmp.userservice.validation.validator.LinksValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class LinksValidatorTest extends BaseUnitTest {

    private LinksValidator linksValidator;

    @BeforeEach
    void setUp() {
        linksValidator = new LinksValidator();
    }

    @Test
    void shouldReturnTrueWhenLinksIsNull() {
        assertTrue(linksValidator.isValid(null, null));
    }

    @Test
    void shouldReturnTrueWhenLinksIsEmpty() {
        assertTrue(linksValidator.isValid(Collections.emptyList(), null));
    }

    @Test
    void shouldReturnFalseWhenLinkTypeIsInvalid() {
        SocialLink invalidLink = new SocialLink();
        invalidLink.setType("invalidLink");
        invalidLink.setUrl("http://validurl.com");
        assertFalse(linksValidator.isValid(Collections.singletonList(invalidLink), null));
    }

    @Test
    void shouldReturnFalseWhenUrlIsInvalid() {
        SocialLink invalidUrlLink = new SocialLink();
        invalidUrlLink.setType("telegram");
        invalidUrlLink.setUrl("ht://invalidurl.com");
        assertFalse(linksValidator.isValid(Collections.singletonList(invalidUrlLink), null));
    }

    @Test
    void shouldReturnTrueWhenLinkIsValid() {
        SocialLink validLink = new SocialLink("vk", "http://validurl.com");
        assertTrue(linksValidator.isValid(Collections.singletonList(validLink), null));
    }
}
