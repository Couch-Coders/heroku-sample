package com.example.herokusample.security;

import com.example.herokusample.security.filter.FirebaseTokenFilter;
import com.example.herokusample.security.filter.TestTokenFilter;
import com.google.firebase.auth.FirebaseAuth;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;


@Profile("prod")
@Configuration
@EnableWebSecurity
public class ProdSecurityConfig extends SecurityConfigBase {
    public ProdSecurityConfig(UserDetailsService userService, FirebaseAuth firebaseAuth) {
        super(new FirebaseTokenFilter(userService, firebaseAuth));
    }
}
