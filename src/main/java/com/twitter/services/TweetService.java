package com.twitter.services;

import com.twitter.dtos.*;
import com.twitter.entities.Tweet;
import com.twitter.entities.User;

import java.util.ArrayList;
import java.util.List;

public interface TweetService {

    List<TweetResponseDto> getAllTweets();

    TweetResponseDto createTweet(TweetRequestDto tweetRequestDto);

    TweetResponseDto deleteTweetById(Long id);

    TweetResponseDto getTweetById(Long id);

    TweetResponseDto likeTweetById(Long id, User user);

    TweetResponseDto replyToTweetById(Long id, Tweet tweet);

    TweetResponseDto repostTweetById(Long id, Tweet tweet);

    List<HashtagDto> getTagsByTweetId(Long id);

    List<UserResponseDto> getLikesByTweetId(Long id);

    ContextDto getContextByTweetId(Long id);

    List<Tweet> getBeforeChain(Tweet tweet);

    List<Tweet> getAfterChain(Tweet tweet);

    List<TweetResponseDto> getRepliesBeforeChain(Tweet tweet, List<Tweet> chain);

    List<TweetResponseDto> getRepliesAfterChain(Tweet tweet, List<Tweet> chain);

    List<Tweet> flattenReplies(List<Tweet> tweets, Tweet targetTweet);

    List<TweetResponseDto> getRepliesByTweetId(Long id);

    List<TweetResponseDto> getRepostsByTweetId(Long id);

    List<TweetResponseDto> getMentionsByTweetId(Long id);
}
