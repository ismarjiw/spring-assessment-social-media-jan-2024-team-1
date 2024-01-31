package com.twitter.services.impl;

import com.twitter.dtos.HashtagDto;
import com.twitter.dtos.TweetResponseDto;
import com.twitter.entities.Hashtag;
import com.twitter.entities.Tweet;
import com.twitter.mappers.TweetMapper;
import org.springframework.stereotype.Service;

import com.twitter.mappers.HashtagMapper;
import com.twitter.repositories.HashtagRepository;
import com.twitter.repositories.TweetRepository;
import com.twitter.services.HashtagService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {
    private final HashtagRepository hashtagRepository;
    private final HashtagMapper hashtagMapper;
private final TweetMapper tweetMapper;
private  final TweetRepository tweetRepository;
public List<HashtagDto> getAllTags(){
    return hashtagMapper.entitiesToDtos(hashtagRepository.findAll());
}
public HashtagDto getRandomTag(){
    List<Hashtag> tags=hashtagRepository.findAll();
    Random rand=new Random();
    Hashtag tag = tags.get((rand.nextInt(tags.size())));
    return hashtagMapper.entityToDto(tag);
}

    @Override
    public List<TweetResponseDto> getTagLabel(String label) {
    Hashtag tag = hashtagRepository.findByLabel(label);
    List<Tweet> tweetsWithTag = tweetRepository.findByhashtag_id(tag.getId());
    return tweetMapper.entitiesToDtos(tweetsWithTag);
    }
}
