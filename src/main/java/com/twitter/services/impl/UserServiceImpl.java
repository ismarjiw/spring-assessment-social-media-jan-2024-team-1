package com.twitter.services.impl;

import com.twitter.dtos.UserRequestDto;
import com.twitter.dtos.UserResponseDto;
import com.twitter.embeddables.Credentials;
import com.twitter.embeddables.Profile;
import com.twitter.entities.User;
import com.twitter.exceptions.BadRequestException;
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

    public List<UserResponseDto> getAllUsersNonDeleted() {

        return userMapper.entitiesToDtos(userRepository.findByDeleted(false));
    }

    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByCredentialsUsername(username);
        if (user == null) {
            throw new NotFoundException("The user you are looking for does not exist with username: " + username);
        }
        return userMapper.entityToDto(user);
    }


    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {


		User result;
        Credentials credentials = credentialsMapper.DtoToEntity(userRequestDto.getCredentials());
		Profile profile = profileMapper.DtoToEntity(userRequestDto.getProfile());
		if (credentials==null){throw new BadRequestException("Credentials cannot be null; username must be unique; Email cannot be null");}
		User existUser=userRepository.findByCredentialsUsernameAndCredentialsPassword(credentials.getUsername(), credentials.getPassword());
		if (existUser!=null && existUser.isDeleted()){
			existUser.setDeleted(false);
			result = userRepository.saveAndFlush(existUser);
		}else {
			User user = new User();
			user.setCredentials(credentials);
			user.setProfile(profile);
			try {
				result=userRepository.saveAndFlush(user);
			} catch (DataIntegrityViolationException e) {
				throw new BadRequestException("Credentials cannot be null; username must be unique; Email cannot be null");
			}
		}

		return userMapper.entityToDto(result);
    }
}
