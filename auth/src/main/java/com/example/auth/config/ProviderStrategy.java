package com.example.auth.config;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface ProviderStrategy {
    Authentication authenticate(HttpServletRequest request);
}
