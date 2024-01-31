package com.twitter.services;

import com.twitter.dtos.CredentialsDto;
import com.twitter.dtos.UserRequestDto;
import com.twitter.dtos.UserResponseDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> getAllUsersNonDeleted();

    UserResponseDto getUserByUsername(String username);

    UserResponseDto createUser(UserRequestDto userRequestDto);

    UserResponseDto updateUser(String usernamem, UserRequestDto userRequestDto);

    UserResponseDto deleteUser(String username, CredentialsDto credentialsDto);


}
