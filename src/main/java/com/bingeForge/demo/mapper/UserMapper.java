package com.bingeForge.demo.mapper;

import com.bingeForge.demo.dto.UserRegisterRequest;
import com.bingeForge.demo.dto.UserResponse;
import com.bingeForge.demo.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUser(UserRegisterRequest userRegisterRequest){
        User user = new User();
        user.setEmail(userRegisterRequest.getEmail());
        user.setUsername(userRegisterRequest.getUsername());
        user.setProfilePicture(userRegisterRequest.getProfilePicture());
        return user;
    }

    public UserResponse toResponse(User user){
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(user.getEmail());
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setProfilePicture(user.getProfilePicture());
        return userResponse;
    }
}
