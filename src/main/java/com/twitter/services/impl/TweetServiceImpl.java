package com.twitter.services.impl;

import com.twitter.dtos.TweetRequestDto;
import com.twitter.dtos.TweetResponseDto;
import com.twitter.entities.Tweet;
import com.twitter.entities.User;
import com.twitter.mappers.HashtagMapper;
import com.twitter.mappers.TweetMapper;
import com.twitter.mappers.UserMapper;
import com.twitter.repositories.HashtagRepository;
import com.twitter.repositories.TweetRepository;
import com.twitter.repositories.UserRepository;
import com.twitter.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetMapper tweetMapper;
    private final TweetRepository tweetRepository;

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    private final HashtagMapper hashtagMapper;
    private final HashtagRepository hashtagRepository;

    @Override
    public List<TweetResponseDto> getAllTweets() {

//        return tweetMapper.entitiesToDtos(tweetRepository.findAll());
        return null;
    }

    @Override
    public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
//        Tweet tweetToSave = tweetMapper.requestDtoToEntity(tweetRequestDto);
//        Tweet savedTweet = tweetRepository.saveAndFlush(tweetToSave);

        return null;
    }

    @Override
    public TweetResponseDto deleteTweetById(Long id) {
        return null;
    }

    @Override
    public TweetResponseDto getTweetById(Long id) {
        return null;
    }

    @Override
    public TweetResponseDto likeTweetById(Long id, User user) {
        return null;
    }

    @Override
    public TweetResponseDto replyToTweetById(Long id, Tweet tweet) {
        return null;
    }

    @Override
    public TweetResponseDto repostTweetById(Long id, Tweet tweet) {
        return null;
    }

    @Override
    public List<TweetResponseDto> getTagsByTweetId(Long id) {
        return null;
    }

    @Override
    public List<TweetResponseDto> getLikesByTweetId(Long id) {
        return null;
    }

    @Override
    public List<TweetResponseDto> getContentByTweetId(Long id) {
        return null;
    }

    @Override
    public List<TweetResponseDto> getRepliesByTweetId(Long id) {
        return null;
    }

    @Override
    public List<TweetResponseDto> getRepostsByTweetId(Long id) {
        return null;
    }

    @Override
    public List<TweetResponseDto> getMentionsByTweetId(Long id) {
        return null;
    }
}
