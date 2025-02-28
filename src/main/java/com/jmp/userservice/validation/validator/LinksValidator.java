package com.jmp.userservice.validation.validator;

import com.jmp.userservice.constant.AllowedLinks;
import com.jmp.userservice.validation.annotation.ValidLinks;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Map;

public class LinksValidator implements ConstraintValidator<ValidLinks, Map<String, String>> {
    @Override
    public boolean isValid(Map<String, String> links, ConstraintValidatorContext context) {
        return links == null || AllowedLinks.getAllowedLinks().containsAll(links.keySet());
    }
}
