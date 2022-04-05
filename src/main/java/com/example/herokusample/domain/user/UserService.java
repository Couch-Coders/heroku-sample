package com.example.herokusample.domain.user;

import java.io.IOException;

import com.example.herokusample.controller.SignupDTO;
import com.example.herokusample.exception.CustomException;
import com.example.herokusample.exception.ErrorCode;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class UserService implements UserDetailsService{
    private final UserRepository userRepository;
    private final FirebaseAuth firebaseAuth;
    private final Bucket bucket;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username)
            .orElseThrow(() -> new UsernameNotFoundException(username + " 유저를 찾을 수 없습니다."));
    }

    @Transactional
    public User signupMock(SignupDTO signupDTO, String token) {
        User user = User.builder().uid(token).name(signupDTO.getName()).build();
        userRepository.save(user);
        return user;
    }

    @Transactional
    public User signup(SignupDTO signupDTO, String token) {
        String uid = verifyToken(token);
        if(userRepository.findById(uid).isPresent()) {
            throw new CustomException(ErrorCode.EXIST_MEMBER);
        }
        User user = User.builder().uid(uid).name(signupDTO.getName()).build();
        userRepository.save(user);
        return user;
    }

    public String verifyToken(String token) {
        try {
            FirebaseToken firebaseToken = firebaseAuth.verifyIdToken(token);
            return firebaseToken.getUid();
        } catch (FirebaseAuthException e) {
            log.error("invalid token", e);
            throw new CustomException(ErrorCode.INVALID_AUTHORIZATION);
        }
    }

    public void saveProfileImage(String uid, byte[] data) {
        Blob blob = bucket.create("profile/" + uid, data);
    }

    public byte[] getProfile(String uid) {
        return bucket.get("profile/" + uid).getContent();
    }

    public User updateProfile(User user, MultipartFile image) {
        String blob = "profile/" + user.getUid();
        try {
            if(bucket.get(blob) != null) {
                bucket.get(blob).delete();
            }
            bucket.create(blob, image.getBytes());
            user.updateProfile("/users/"+user.getUid()+"/profile");
            userRepository.save(user);
            return user;
            
        } catch (IOException e) {
            log.error(user.getUid() + " profile upload faild", e);
            throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }
    
}
