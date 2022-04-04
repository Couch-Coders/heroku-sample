package com.example.herokusample.security;

import com.example.herokusample.security.filter.TestTokenFilter;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Profile("local")
@Configuration
@EnableWebSecurity
public class TestSecurityConfig extends SecurityConfigBase {
    public TestSecurityConfig() {
        super(new TestTokenFilter());
    }
}