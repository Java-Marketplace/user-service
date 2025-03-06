package com.jmp.userservice.validation.validator;

import com.jmp.userservice.model.AllowedLinks;
import com.jmp.userservice.model.SocialLink;
import com.jmp.userservice.validation.annotation.ValidLinks;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class LinksValidator implements ConstraintValidator<ValidLinks, List<SocialLink>> {

    @Override
    public boolean isValid(List<SocialLink> links, ConstraintValidatorContext context) {
        if (links == null || links.isEmpty()) {
            return true;
        }

        for (SocialLink link : links) {
            if (link.getType() == null || !AllowedLinks.getAllowedLinks().contains(link.getType())) {
                return false;
            }

            if (link.getUrl() == null || !isValidUrl(link.getUrl())) {
                return false;
            }
        }

        return true;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean isValidUrl(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }

        try {
            new URI(url).toURL();
            return true;
        } catch (URISyntaxException | MalformedURLException e) {
            return false;
        }
    }
}
