package com.twitter.services.impl;

import com.twitter.dtos.*;
import com.twitter.embeddables.Credentials;
import com.twitter.entities.Hashtag;
import com.twitter.entities.Tweet;
import com.twitter.entities.User;
import com.twitter.exceptions.BadRequestException;
import com.twitter.exceptions.NotAuthorizedException;
import com.twitter.exceptions.NotFoundException;
import com.twitter.mappers.CredentialsMapper;
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

    private final CredentialsMapper credentialsMapper;

    private static final String TWEET_NOT_FOUND_MSG = "Tweet not found with ID: ";
    private static final String BAD_REQUEST_MSG = "Error while processing the request ";
    private static final String TAGS_NOT_FOUND_MSG = "Tags not found with ID: ";
    private static final String USER_NOT_FOUND_MSG = "User not found in the database";

    @Override
    public List<TweetResponseDto> getAllTweets() {

        return tweetMapper.entitiesToDtos(tweetRepository.findAll());
    }

    @Override
    public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {

        Credentials credentials = credentialsMapper.dtoToEntity(tweetRequestDto.getCredentials());
        Optional<User> optionalUser = userRepository.findByCredentials(credentials);

        try {
            if (optionalUser.isPresent()) {
                User author = optionalUser.get();
                Tweet tweetToSave = tweetMapper.requestDtoToEntity(tweetRequestDto);
                tweetToSave.setAuthor(author);

                Tweet savedTweet = tweetRepository.saveAndFlush(tweetToSave);

                return tweetMapper.entityToDto(savedTweet);
            } else {
                throw new NotFoundException(USER_NOT_FOUND_MSG);
            }
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_MSG);
        }
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
    public void likeTweetById(Long id, CredentialsDto credentialsDto) {

        Optional<Tweet> optionalTweet = tweetRepository.findById(id);
        Credentials credentials = credentialsMapper.dtoToEntity(credentialsDto);
        Optional<User> optionalUser = userRepository.findByCredentials(credentials);
        // com.twitter.exceptions.NotFoundException: User not found
        try {
            if (optionalUser.isPresent() && optionalTweet.isPresent()) {
                User user = optionalUser.get();
                Tweet selectedTweet = optionalTweet.get();

                if (!user.getLikedTweets().contains(selectedTweet)) {
                    user.getLikedTweets().add(selectedTweet);
                    userRepository.saveAndFlush(user);
                }
            } else {
                if (!optionalUser.isPresent()) {
                    throw new NotFoundException("User not found");
                }
                if (!optionalTweet.isPresent()) {
                    throw new NotFoundException("Tweet not found");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException(BAD_REQUEST_MSG);
        }
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

                List<User> userLikes = new ArrayList<>();
                for (User u: selectedTweet.getLikedByUsers()) {
                    if (!u.isDeleted()) {
                        userLikes.add(u);
                    }
                }

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

            // Get the before and after chains in chronological order
            List<Tweet> beforeChain = getBeforeChain(selectedTweet);
            List<Tweet> afterChain = getAfterChain(selectedTweet);

            // Flatten the afterChain -> need to get nested replies
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

            Tweet previousTweet = tweet.getInReplyTo();
            if (previousTweet != null) {
                getRepliesBeforeChain(previousTweet, chain);
            }
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
        Optional<Tweet> optionalTweet = tweetRepository.findById(id);

        try {
            if (optionalTweet.isPresent()) {
                Tweet selectedTweet = optionalTweet.get();
                List<Tweet> replies = selectedTweet.getReplies();
                return tweetMapper.entitiesToDtos(replies);
            } else {
                throw new NotFoundException(TWEET_NOT_FOUND_MSG + id);
            }
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_MSG);
        }
    }

    @Override
    public List<TweetResponseDto> getRepostsByTweetId(Long id) {
        Optional<Tweet> optionalTweet = tweetRepository.findById(id);

        try {
            if (optionalTweet.isPresent()) {
                Tweet selectedTweet = optionalTweet.get();
                List<Tweet> reposts = selectedTweet.getReposts();
                return tweetMapper.entitiesToDtos(reposts);
            } else {
                throw new NotFoundException(TWEET_NOT_FOUND_MSG + id);
            }
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_MSG);
        }
    }

    @Override
    public List<UserResponseDto> getMentionsByTweetId(Long id) {
        Optional<Tweet> optionalTweet = tweetRepository.findById(id);

        try {
            if (optionalTweet.isPresent()) {
                Tweet selectedTweet = optionalTweet.get();
                List<User> mentionedUsers = selectedTweet.getMentionedUsers();
                return userMapper.entitiesToDtos(mentionedUsers);
            } else {
                throw new NotFoundException(TWEET_NOT_FOUND_MSG + id);
            }
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_MSG);
        }
    }
}
