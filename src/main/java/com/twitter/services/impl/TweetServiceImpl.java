package com.twitter.services.impl;

import com.twitter.dtos.*;
import com.twitter.entities.Hashtag;
import com.twitter.entities.Tweet;
import com.twitter.entities.User;
import com.twitter.exceptions.BadRequestException;
import com.twitter.exceptions.NotFoundException;
import com.twitter.mappers.HashtagMapper;
import com.twitter.mappers.TweetMapper;
import com.twitter.mappers.UserMapper;
import com.twitter.repositories.HashtagRepository;
import com.twitter.repositories.TweetRepository;
import com.twitter.repositories.UserRepository;
import com.twitter.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetMapper tweetMapper;
    private final TweetRepository tweetRepository;

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    private final HashtagMapper hashtagMapper;
    private final HashtagRepository hashtagRepository;

    String TWEET_NOT_FOUND_MSG = "Tweet not found with ID: ";
    String BAD_REQUEST_MSG = "Error while processing the request ";
    String TAGS_NOT_FOUND_MSG = "Tags not found with ID: ";

    @Override
    public List<TweetResponseDto> getAllTweets() {

        return tweetMapper.entitiesToDtos(tweetRepository.findAll());
    }

    @Override
    public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {

        return null;
    }

    @Override
    public TweetResponseDto deleteTweetById(Long id) {
        return null;
    }

    @Override
    public TweetResponseDto getTweetById(Long id) {

        Optional<Tweet> optionalTweet = tweetRepository.findById(id);

        try {
            if (optionalTweet.isPresent()) {
                Tweet selectedTweet = optionalTweet.get();
                return tweetMapper.entityToDto(selectedTweet);
            } else {
                throw new NotFoundException(TWEET_NOT_FOUND_MSG + id);
            }
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_MSG);
        }
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
    public List<HashtagDto> getTagsByTweetId(Long id) {

        Optional<Tweet> optionalTweet = tweetRepository.findById(id);

        try {
            if (optionalTweet.isPresent()) {
                Tweet selectedTweet = optionalTweet.get();
                List<Hashtag> associatedTags = selectedTweet.getHashtags();
                return hashtagMapper.entitiesToDtos(associatedTags);
            } else {
                throw new NotFoundException(TAGS_NOT_FOUND_MSG + id);
            }
        } catch (Exception e) {
        throw new BadRequestException(BAD_REQUEST_MSG);
    }
    }

    @Override
    public List<UserResponseDto> getLikesByTweetId(Long id) {

        Optional<Tweet> optionalTweet = tweetRepository.findById(id);

        try {
            if (optionalTweet.isPresent()) {
                Tweet selectedTweet = optionalTweet.get();
                List<User> userLikes = selectedTweet.getLikedByUsers();
                return userMapper.entitiesToDtos(userLikes);
            } else {
                throw new NotFoundException(TAGS_NOT_FOUND_MSG + id);
            }
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_MSG);
        }
    }

    @Override
    public ContextDto getContextByTweetId(Long id) {

        Optional<Tweet> optionalTweet = tweetRepository.findById(id);

        if (optionalTweet.isPresent()) {
            Tweet selectedTweet = optionalTweet.get();

            // Get the before and after chains
            List<Tweet> beforeChain = getBeforeChain(selectedTweet);
            List<Tweet> afterChain = getAfterChain(selectedTweet);

            // Flatten the afterChain -> need to capture nested replies in chronological order
            List<Tweet> flattenedAfterChain = flattenReplies(afterChain, selectedTweet);

            // Map the chains to DTOs
            List<TweetResponseDto> beforeDtoChain = tweetMapper.entitiesToDtos(beforeChain);
            List<TweetResponseDto> afterDtoChain = tweetMapper.entitiesToDtos(flattenedAfterChain);

            // Map the target tweet to DTO
            TweetResponseDto targetDto = tweetMapper.entityToDto(selectedTweet);

            // Create ContextDto setting target, before, and after
            ContextDto contextDto = new ContextDto();
            contextDto.setTarget(targetDto);
            contextDto.setBefore(beforeDtoChain);
            contextDto.setAfter(afterDtoChain);

            return contextDto;
        } else {
            throw new NotFoundException(TWEET_NOT_FOUND_MSG + id);
        }
    }

    @Override
    public List<Tweet> getBeforeChain(Tweet tweet) {
        List<Tweet> chain = new ArrayList<>();
        getRepliesBeforeChain(tweet.getInReplyTo(), chain);
        return chain.stream()
                .sorted(Comparator.comparing(Tweet::getPosted))
                .toList();
    }

    @Override
    public List<Tweet> getAfterChain(Tweet tweet) {
        List<Tweet> chain = new ArrayList<>();
        getRepliesAfterChain(tweet.getInReplyTo(), chain);
        return chain.stream()
                .sorted(Comparator.comparing(Tweet::getPosted))
                .toList();
    }

    @Override
    public List<TweetResponseDto> getRepliesBeforeChain(Tweet tweet, List<Tweet> chain) {
        if (tweet != null) {
            if (chain == null) {
                chain = new ArrayList<>();
            }
            chain.add(tweet);
        }
        return tweetMapper.entitiesToDtos(chain);
    }

    @Override
    public List<TweetResponseDto> getRepliesAfterChain(Tweet tweet, List<Tweet> chain) {

        if (tweet != null) {
            if (chain == null) {
                chain = new ArrayList<>();
            }
            chain.addAll(tweet.getReplies());
        }
        return tweetMapper.entitiesToDtos(chain);
    }

    @Override
    public List<Tweet> flattenReplies(List<Tweet> tweets, Tweet targetTweet) {
        List<Tweet> flattenedList = new ArrayList<>();
        for (Tweet tweet : tweets) {
            if (!tweet.getId().equals(targetTweet.getId())) {
                flattenedList.add(tweet);
            }
            flattenedList.addAll(flattenReplies(tweet.getReplies(), targetTweet));
        }
        return flattenedList;
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
