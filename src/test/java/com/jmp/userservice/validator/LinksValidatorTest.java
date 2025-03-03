package com.jmp.userservice.validator;

import com.jmp.userservice.validation.validator.LinksValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class LinksValidatorTest {
    private final LinksValidator linksValidator = new LinksValidator();
    private final ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

    @Test
    void isValid_ShouldReturnTrue_WhenLinksIsNull(){
        assertTrue(linksValidator.isValid(null, context));
    }

    @Test
    void isValidShouldReturnTrue_WhenLinksContainsOnlyAllowedKeys(){
        Map<String, String> links = Map.of("telegram", "telegram", "vk", "vk");
        assertTrue(linksValidator.isValid(links, context));
    }

    @Test
    void isValidShouldReturnFalse_WhenLinksContainsDissolvedKeys(){
        Map<String, String> links = Map.of("instagram", "telegram", "facebook", "vk");
        assertFalse(linksValidator.isValid(links, context));
    }

    @Test
    void isValid_ShouldReturnTrue_WhenLinksIsEmpty(){
        Map<String, String> links = Map.of();
        assertTrue(linksValidator.isValid(links, context));
    }
}
