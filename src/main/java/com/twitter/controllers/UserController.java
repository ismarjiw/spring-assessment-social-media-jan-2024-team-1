package com.twitter.controllers;

import com.twitter.dtos.CredentialsDto;
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

    @PatchMapping("/@{username}")
    public UserResponseDto updateUser(@PathVariable String username, @RequestBody UserRequestDto userRequestDto){
        return userService.updateUser(username, userRequestDto);
    }


    @DeleteMapping("/@{username}")
    public UserResponseDto deleteUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto){
        return userService.deleteUser(username,credentialsDto);
    }
    @GetMapping("/@{username}/following")
    public List<UserResponseDto> getAllFollowings(@PathVariable String username){
        return userService.getAllFollowings(username);
    }
    @GetMapping("/@{username}/followers")
    public List<UserResponseDto> getAllFollowers(@PathVariable String username){
        return userService.getAllFollowers(username);
    }

    @PostMapping("/@{username}/follow")
    public void createFollowRelationship(@PathVariable String username, @RequestBody CredentialsDto credentialsDto){
        userService.createFollowRelationship(username,credentialsDto);
    }


    @PostMapping("/@{username}/unfollow")
    public void removeFollowRelationship(@PathVariable String username, @RequestBody CredentialsDto credentialsDto){
        userService.removeFollowRelationship(username,credentialsDto);
    }
}

