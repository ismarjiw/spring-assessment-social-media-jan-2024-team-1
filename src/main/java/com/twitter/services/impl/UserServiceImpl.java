package com.twitter.services.impl;

import com.twitter.dtos.CredentialsDto;
import com.twitter.dtos.UserRequestDto;
import com.twitter.dtos.UserResponseDto;
import com.twitter.embeddables.Credentials;
import com.twitter.embeddables.Profile;
import com.twitter.entities.User;
import com.twitter.exceptions.BadRequestException;
import com.twitter.exceptions.NotAuthorizedException;
import com.twitter.exceptions.NotFoundException;
import com.twitter.mappers.ProfileMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.twitter.mappers.CredentialsMapper;
import com.twitter.mappers.TweetMapper;
import com.twitter.mappers.UserMapper;
import com.twitter.repositories.TweetRepository;
import com.twitter.repositories.UserRepository;
import com.twitter.services.UserService;
import com.twitter.services.ValidateService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProfileMapper profileMapper;
    private final CredentialsMapper credentialsMapper;

    @Override
    public List<UserResponseDto> getAllUsersNonDeleted() {

        return userMapper.entitiesToDtos(userRepository.findByDeleted(false));
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByCredentialsUsername(username);
        if (user == null) {
            throw new NotFoundException("The user you are looking for does not exist with username: " + username);
        }
        return userMapper.entityToDto(user);
    }


    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        if (userRequestDto.getCredentials() == null || userRequestDto.getCredentials().getUsername() == null || userRequestDto.getCredentials().getPassword() == null || userRequestDto.getProfile() == null || userRequestDto.getProfile().getEmail() == null) {
            throw new BadRequestException("Credentials cannot be null; username must be unique; Email cannot be null");
        }
        User user;
        Credentials credentials = credentialsMapper.DtoToEntity(userRequestDto.getCredentials());
        Profile profile = profileMapper.DtoToEntity(userRequestDto.getProfile());
        user = userRepository.findByCredentialsUsernameAndCredentialsPassword(credentials.getUsername(), credentials.getPassword());
        if (user != null && user.isDeleted()) {
            user.setDeleted(false);
            user.setProfile(profile);
        } else {
            user = new User();
            user.setCredentials(credentials);
            user.setProfile(profile);
        }
        try {
            userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Credentials cannot be null; username must be unique; Email cannot be null");
        }
        return userMapper.entityToDto(user);
    }

    @Override
    public UserResponseDto updateUser(String username, UserRequestDto userRequestDto) {
        if (userRequestDto.getCredentials() == null || userRequestDto.getCredentials().getUsername() == null || userRequestDto.getCredentials().getPassword() == null) {
            throw new BadRequestException("Credentials cannot be null.");
        }
        CredentialsDto credentials = userRequestDto.getCredentials();
        User user = userRepository.findByCredentialsUsername(username);
        if (user == null) {
            throw new NotFoundException("Not found user with username: " + username);
        } else if (!user.getCredentials().getUsername().equals(username)) {
            throw new NotAuthorizedException("Credentials not vaild");
        }
        if(userRequestDto.getProfile()==null){
            throw new BadRequestException("Profile (Email) cannot be null");
        }

            Profile profile = profileMapper.DtoToEntity(userRequestDto.getProfile());
            user.setCredentials(credentialsMapper.DtoToEntity(credentials));
            if (profile.getFirstName()!=null){
                user.getProfile().setFirstName(profile.getFirstName());
            }
            if (profile.getLastName()!=null){
                user.getProfile().setLastName(profile.getLastName());
            }
            if(profile.getEmail()!=null){
                user.getProfile().setEmail(profile.getEmail());
            }
            if (profile.getPhone()!=null){
                user.getProfile().setPhone(profile.getPhone());
            }


        user = userRepository.saveAndFlush(user);

        return userMapper.entityToDto(user);

    }
}
