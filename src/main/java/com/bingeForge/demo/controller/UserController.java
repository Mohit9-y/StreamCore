package com.bingeForge.demo.controller;

import com.bingeForge.demo.dto.*;
import com.bingeForge.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(@Valid @PathVariable UUID id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(id));
    }

    @PutMapping("/update_user_name")
    public ResponseEntity<UserResponse> updateUserName(@Valid @RequestBody UpdateUserNameRequest req){
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserName(req));
    }

    @PutMapping("/update_user_email")
    public ResponseEntity<UserResponse> updateUserName(@Valid @RequestBody UpdateUserEmailRequest req){
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserEmail(req));
    }

    @PutMapping("/update_profile_picture")
    public ResponseEntity<UserResponse> updateUserProfilePicture(@Valid @RequestBody UpdateUserProfilePictureRequest req){
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserProfilePicture(req));
    }

    @PutMapping("/update_password")
    public ResponseEntity<Void> updateUserPassword(@Valid @RequestBody UpdatePasswordRequest req){
        userService.updatePassword(req);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@Valid @PathVariable UUID id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
