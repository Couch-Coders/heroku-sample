package com.example.herokusample.controller;

import com.example.herokusample.domain.user.User;
import com.example.herokusample.domain.user.UserService;
import com.example.herokusample.util.RequestUtil;
import com.google.api.client.util.Value;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RequestMapping("/users")
@RestController
@Slf4j
public class UserController {
    private String activeProfile;
    private final UserService userService;

    @Autowired
    public UserController(UserService userService,
                         Environment environments) {
        this.userService = userService;
        this.activeProfile = environments.getActiveProfiles()[0];
        log.info("activeProfile: {}", activeProfile);
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

    @PostMapping("/me/profile")
    public User updateProfile(@RequestParam MultipartFile image,
                              Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("user: {}", user);
        return userService.updateProfile(user, image);
    }

    @GetMapping("/{uid}/profile")
    public byte[] downloadProfile(@PathVariable String uid) {
        return userService.getProfile(uid);
    }
}
