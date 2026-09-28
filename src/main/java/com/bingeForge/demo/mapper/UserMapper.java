package com.bingeForge.demo.mapper;

import com.bingeForge.demo.dto.auth.UserRegisterRequest;
import com.bingeForge.demo.dto.user.UserResponse;
import com.bingeForge.demo.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUser(UserRegisterRequest userRegisterRequest){
        User user = new User();

        // Records use the field name as the accessor method, not "get"
        user.setEmail(userRegisterRequest.email());
        user.setUsername(userRegisterRequest.username());
        user.setProfilePicture(userRegisterRequest.profilePicture());

        return user;
    }

    public UserResponse toResponse(User user){
        // Records are immutable, so we use the Lombok Builder to construct it
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profilePicture(user.getProfilePicture())
                .role(user.getRole()) // Added this since it's required by your UserResponse record
                .build();
    }
}