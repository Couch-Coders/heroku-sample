package com.example.herokusample.security;

import com.example.herokusample.domain.user.UserService;
import com.example.herokusample.security.filter.FirebaseTokenFilter;
import com.google.firebase.auth.FirebaseAuth;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@Profile("prod")
@Configuration
@EnableWebSecurity
public class ProdSecurityConfig extends SecurityConfigBase {
    public ProdSecurityConfig(UserService userService, FirebaseAuth firebaseAuth) {
        super(new FirebaseTokenFilter(userService, firebaseAuth));
    }
}
