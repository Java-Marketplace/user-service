package com.jmp.userservice.validator;

import com.jmp.userservice.model.SocialLink;
import com.jmp.userservice.validation.validator.LinksValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class LinksValidatorTest {
    private final LinksValidator linksValidator = new LinksValidator();
    private final ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

    @Test
    void isValid_ShouldReturnTrue_WhenLinksIsNull() {
        assertTrue(linksValidator.isValid(null, context));
    }

    @Test
    void isValid_ShouldReturnTrue_WhenLinksContainsOnlyAllowedKeys() {
        List<SocialLink> links = List.of(
                new SocialLink("telegram", "https://telegram.com"),
                new SocialLink("vk", "https://vk.com")
        );
        assertTrue(linksValidator.isValid(links, context));
    }

    @Test
    void isValid_ShouldReturnFalse_WhenLinksContainsDisallowedKeys() {
        SocialLink firstInvalidLink = new SocialLink("telegram", "https://telegram.com");
        firstInvalidLink.setType("invalid");
        SocialLink secondInvalidLink = new SocialLink("vk", "https://vk.com");
        secondInvalidLink.setType("invalid");

        List<SocialLink> invalidLinks = new ArrayList<>();
        invalidLinks.add(firstInvalidLink);
        invalidLinks.add(secondInvalidLink);

        assertFalse(linksValidator.isValid(invalidLinks, context));
    }

    @Test
    void isValid_ShouldReturnTrue_WhenLinksIsEmpty() {
        List<SocialLink> links = new ArrayList<>();
        assertTrue(linksValidator.isValid(links, context));
    }
}
