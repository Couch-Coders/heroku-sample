package com.example.herokusample.domain.user;

import com.example.herokusample.controller.SignupDTO;
import com.example.herokusample.exception.CustomException;
import com.example.herokusample.exception.ErrorCode;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class UserService implements UserDetailsService{
    private final UserRepository userRepository;
    private final FirebaseAuth firebaseAuth;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username)
            .orElseThrow(() -> new UsernameNotFoundException(username + " 유저를 찾을 수 없습니다."));
    }

    public User signupMock(SignupDTO signupDTO, String token) {
        User user = User.builder().uid(token).name(signupDTO.getUsername()).build();
        userRepository.save(user);
        return user;
    }

    public User signup(SignupDTO signupDTO, String token) {
        try {
            FirebaseToken firebaseToken = firebaseAuth.verifyIdToken(token);
            if(userRepository.findById(firebaseToken.getUid()).isPresent()) {
                throw new CustomException(ErrorCode.EXIST_MEMBER);
            }
            User user = User.builder().uid(firebaseToken.getUid()).name(signupDTO.getUsername()).build();
            userRepository.save(user);
            return user;
        } catch (FirebaseAuthException e) {
            log.error("invalid token", e);
            throw new CustomException(ErrorCode.INVALID_AUTHORIZATION);
        }
    }
    
}
