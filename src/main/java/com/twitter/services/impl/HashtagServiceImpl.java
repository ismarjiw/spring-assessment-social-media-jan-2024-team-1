package com.twitter.services.impl;

import org.springframework.stereotype.Service;

import com.twitter.mappers.HashtagMapper;
import com.twitter.repositories.HashtagRepository;
import com.twitter.repositories.TweetRepository;
import com.twitter.services.HashtagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {
	private final HashtagRepository hashtagRepository;
    private final HashtagMapper hashtagMapper;
    private final TweetRepository tweetRepository;
}
