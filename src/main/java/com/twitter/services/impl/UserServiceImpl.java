package com.twitter.services.impl;

import com.twitter.dtos.UserRequestDto;
import com.twitter.dtos.UserResponseDto;
import com.twitter.entities.User;
import com.twitter.exceptions.NotFoundException;
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

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final UserMapper userMapper;

	public List<UserResponseDto> getAllUsersNonDeleted(){

		return userMapper.entitiesToDtos(userRepository.findByDeleted(false));
	}
	public UserResponseDto getUserByUsername(String username){
		User user=userRepository.findByCredentialsUsername(username);
		if (user==null){
			throw new NotFoundException("The user you are looking for does not exist with username: "+username);
		}
		return userMapper.entityToDto(user);
	}
}
