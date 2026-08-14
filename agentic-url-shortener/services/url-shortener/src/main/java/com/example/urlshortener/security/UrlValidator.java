package com.example.urlshortener.security;

import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Set;

@Component
public class UrlValidator {
    private static final Set<String> ALLOWED = Set.of("http", "https");

    public void validate(String value) {
        try {
            URI uri = URI.create(value);
            if (!ALLOWED.contains(uri.getScheme())) {
                throw new IllegalArgumentException("Only http and https URLs are allowed");
            }
            if (uri.getHost() == null) {
                throw new IllegalArgumentException("URL must contain a host");
            }

            InetAddress address = InetAddress.getByName(uri.getHost());
            if (address.isAnyLocalAddress()
                    || address.isLoopbackAddress()
                    || address.isLinkLocalAddress()
                    || address.isSiteLocalAddress()
                    || address.isMulticastAddress()) {
                throw new IllegalArgumentException("Private or local destinations are not allowed");
            }
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("URL host cannot be resolved");
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid URL");
        }
    }
}
