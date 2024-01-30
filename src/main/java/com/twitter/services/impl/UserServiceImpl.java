package com.twitter.services.impl;

import org.springframework.stereotype.Service;

import com.twitter.mappers.CredentialsMapper;
import com.twitter.mappers.TweetMapper;
import com.twitter.mappers.UserMapper;
import com.twitter.repositories.TweetRepository;
import com.twitter.repositories.UserRepository;
import com.twitter.services.UserService;
import com.twitter.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final CredentialsMapper credentialsMapper;
	private final TweetMapper tweetMapper;
	private final TweetRepository tweetRepository;
	private final ValidateService validateService;
}
