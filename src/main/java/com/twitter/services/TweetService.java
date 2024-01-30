package com.twitter.services;

import com.twitter.dtos.TweetRequestDto;
import com.twitter.dtos.TweetResponseDto;
import com.twitter.entities.Tweet;
import com.twitter.entities.User;

import java.util.List;

public interface TweetService {

    List<TweetResponseDto> getAllTweets();

    TweetResponseDto createTweet(TweetRequestDto tweetRequestDto);

    TweetResponseDto deleteTweetById(Long id);

    TweetResponseDto getTweetById(Long id);

    TweetResponseDto likeTweetById(Long id, User user);

    TweetResponseDto replyToTweetById(Long id, Tweet tweet);

    TweetResponseDto repostTweetById(Long id, Tweet tweet);

    TweetResposneDto getTagsByTweetId(Long id);

    TweetResposneDto getLikesByTweetId(Long id);

    TweetResposneDto getContentByTweetId(Long id);

    TweetResposneDto getRepliesByTweetId(Long id);

    TweetResposneDto getRepostsByTweetId(Long id);

    TweetResposneDto getMentionsByTweetId(Long id);
}
