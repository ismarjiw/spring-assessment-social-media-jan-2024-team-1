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

    @Override
    public boolean checkHashtagExists(String label) {
        return (hashtagRepository.findByLabel(label)!=null);
    }

    @Override
    public boolean checkUserExists(String username) {
        return (userRepository.findByCredentialsUsername(username)!=null);
    }

    @Override
    public boolean checkUsernameAvailable(String username) {
        return (userRepository.findByCredentialsUsername(username)==null);
    }
}
