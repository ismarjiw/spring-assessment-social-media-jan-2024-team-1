package com.twitter.controllers;

import com.twitter.dtos.UserRequestDto;
import com.twitter.dtos.UserResponseDto;
import com.twitter.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    @GetMapping
    public List<UserResponseDto> getAllUsersNonDeleted(){
        return userService.getAllUsersNonDeleted();
    }

    @GetMapping("/@{username}")
    public UserResponseDto getUserByUsername(@PathVariable String username){
        return userService.getUserByUsername(username);
    }
    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto userrequestDto){
        System.out.print("testing2123e4");
        return userService.createUser(userrequestDto);
    }
}

