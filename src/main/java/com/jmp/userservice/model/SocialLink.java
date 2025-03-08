package com.jmp.userservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

@Embeddable
@Getter
@Setter
public class SocialLink {

    @Column(name = "link_type", nullable = false)
    private String type;

    @Column(name = "url", nullable = false)
    private String url;

    public SocialLink() {
    }

    public SocialLink(String type, String url) {
        if (!AllowedLinks.getAllowedLinks().contains(type)) {
            throw new IllegalArgumentException("Social link type not allowed: " + type);
        }
        if (!isValidUrl(url)) {
            throw new IllegalArgumentException("Social link url is invalid: " + url);
        }
        this.type = type;
        this.url = url;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean isValidUrl(String url) {
        try {
            new URI(url).toURL();
            return true;
        } catch (URISyntaxException | MalformedURLException | IllegalArgumentException e) {
            return false;
        }
    }

}
