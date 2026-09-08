package com.bingeForge.demo.service;

import com.bingeForge.demo.dto.*;
import com.bingeForge.demo.entity.User;
import com.bingeForge.demo.exception.ResourceNotFoundException;
import com.bingeForge.demo.mapper.UserMapper;
import com.bingeForge.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, UserMapper userMapper,PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    private User findById(UUID id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Their is no user with id : " + id ));
        return user;
    }

    @Transactional
    public User registerUser(UserRegisterRequest req){
        if(userRepository.existsByEmail(req.email())){
            throw new IllegalArgumentException("Email already in use");
        }
        if(userRepository.existsByUsername(req.username())){
            throw new IllegalArgumentException("UserName already exist");
        }
        User user = userMapper.toUser(req);
        user.setHashedPassword(passwordEncoder.encode(req.password()));
        return userRepository.save(user);
    }

    public UserResponse getUser(UUID id){
        User user = findById(id);
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateUserName(UpdateUserNameRequest req){
        User user = findById(req.id());
        if(userRepository.existsByUsername(req.userName())){
            throw new IllegalArgumentException("userName already exist");
        }
        user.setUsername(req.userName());
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateUserEmail(UpdateUserEmailRequest req){
        User user = findById(req.id());
        if(userRepository.existsByEmail(req.email())){
            throw new IllegalArgumentException("email already exist");
        }

        user.setEmail(req.email());
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateUserProfilePicture(UpdateUserProfilePictureRequest req){
        User user = findById(req.id());
        user.setProfilePicture(req.profilePicture());
        return userMapper.toResponse(user);
    }

    @Transactional
    public void updatePassword(UpdatePasswordRequest req){
        User user = findById(req.id());
        if(!passwordEncoder.matches(req.oldPassword(), user.getHashedPassword())){
            throw new BadCredentialsException("Current password does not match");
        }

        if (passwordEncoder.matches(req.newPassword(), user.getHashedPassword())) {
            throw new IllegalArgumentException("New password cannot be the same as the old password");
        }

        user.setHashedPassword(passwordEncoder.encode(req.newPassword()));
    }

    @Transactional
    public void deleteUser(UUID id){
        User user = findById(id);
        userRepository.delete(user);
    }
}
