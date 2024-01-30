package com.twitter.services.impl;

import org.springframework.stereotype.Service;

import com.twitter.repositories.HashtagRepository;
import com.twitter.repositories.UserRepository;
import com.twitter.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {
	private final HashtagRepository hashtagRepository;
    private final UserRepository userRepository;
}
