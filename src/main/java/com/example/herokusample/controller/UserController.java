package com.example.herokusample.controller;

import com.example.herokusample.domain.user.User;
import com.example.herokusample.domain.user.UserService;
import com.example.herokusample.util.RequestUtil;
import com.google.api.client.util.Value;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RequestMapping("/users")
@RestController
public class UserController {
    @Value("${spring.profiles.active}")
    private String activeProfile;
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/me")
    public User login(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    @PostMapping("")
    public User signup(@RequestBody SignupDTO signupDTO,
                       @RequestHeader("Authorization") String authorization) {
        String token = RequestUtil.getAuthorizationToken(authorization);
        if(activeProfile.equals("local")) {
            return userService.signupMock(signupDTO, token);
        } else {
            return userService.signup(signupDTO, token);
        }
    }
}
