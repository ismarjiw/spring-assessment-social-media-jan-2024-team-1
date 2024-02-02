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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
//        if (tweetRequestDto.getCredentials() == null || tweetRequestDto.getContent() == null) {
//            throw new BadRequestException(BAD_REQUEST_MSG);
//        }
        Credentials credentials = credentialsMapper.dtoToEntity(tweetRequestDto.getCredentials());
        Optional<User> optionalUser = userRepository.findByCredentials(credentials);

        try {
            if (optionalUser.isPresent()) {
                User author = optionalUser.get();

                Tweet tweetToSave = tweetMapper.requestDtoToEntity(tweetRequestDto);
                tweetToSave.setAuthor(author);
                List<User> mentionedUsers = extractMentions(tweetToSave.getContent());
                tweetToSave.setMentionedUsers(mentionedUsers);

                List<Hashtag> potentialtags = extractTags(tweetToSave.getContent());
                tweetToSave.setHashtags(potentialtags);

                Tweet savedTweet = tweetRepository.saveAndFlush(tweetToSave);
                return tweetMapper.entityToDto(savedTweet);
            } else {
                throw new NotFoundException(USER_NOT_FOUND_MSG);
            }
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_MSG);
        }
    }

    public List<User> extractMentions(String tweetContent) {
        List<User> mentions = new ArrayList<>();

        // Define a regex pattern for mentions
        Pattern mentionsPattern = Pattern.compile("@[\\w_]+");

        // Create a matcher with the tweet content
        Matcher matcher = mentionsPattern.matcher(tweetContent);

        // Find all mentions and add them to the list
        while (matcher.find()) {
            String username = matcher.group().substring(1);
            User user = userRepository.findByCredentialsUsername(username);

            if (user != null) {
                mentions.add(user);

                System.out.print(user.getCredentials().getUsername());
            }

        }

        return mentions;
    }

    public List<Hashtag> extractTags(String tweetContent) {
        List<Hashtag> tags = new ArrayList<>();

        // Define a regex pattern for mentions
        Pattern tagPattern = Pattern.compile("#[\\w_]+");

        // Create a matcher with the tweet content
        Matcher matcher = tagPattern.matcher(tweetContent);

        // Find all mentions and add them to the list
        while (matcher.find()) {
            String tagLabel = matcher.group().substring(1);
            Hashtag tag = hashtagRepository.findByLabel(tagLabel);
            if (tag != null) {
                tags.add(tag);
            } else {
                Hashtag newtag = new Hashtag();
                newtag.setLabel(tagLabel);
                hashtagRepository.save(newtag);
                tags.add(newtag);
            }

        }

        return tags;
    }

    @Override
    public TweetResponseDto deleteTweetById(Long id, CredentialsDto credentialsDto) {

        Optional<Tweet> tweetToDelete = tweetRepository.findById(id);
        Credentials credentials = credentialsMapper.dtoToEntity(credentialsDto);
        Optional<User> optionalUser = userRepository.findByCredentials(credentials);

        if (!tweetToDelete.isPresent() || tweetToDelete.get().isDeleted()) {
            throw new NotFoundException("Tweet not found");
        }

        if (!optionalUser.isPresent()) {
            throw new NotFoundException("User not found");
        }

        try {
            Tweet deletedTweet = tweetToDelete.get();
            Long authorOfTweetById = deletedTweet.getAuthor().getId();
            User currentUser = optionalUser.get();

            if (authorOfTweetById.equals(currentUser.getId())) {
                deletedTweet.setDeleted(true);
                tweetRepository.saveAndFlush(deletedTweet);
                return tweetMapper.entityToDto(deletedTweet);
            } else {
                throw new NotAuthorizedException("User not allowed to delete this tweet");
            }
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_MSG);
        }
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

        if (!optionalTweet.isPresent() || optionalTweet.get().isDeleted()) {
            throw new NotFoundException("Tweet not found");
        }

        if (!optionalUser.isPresent()) {
            throw new NotFoundException("User not found");
        }

        User user = optionalUser.get();
        Tweet selectedTweet = optionalTweet.get();

        if (!user.getLikedTweets().contains(selectedTweet)) {
            user.getLikedTweets().add(selectedTweet);
            userRepository.saveAndFlush(user);
        }
    }

    @Override
    public TweetResponseDto replyToTweetById(Long id, TweetRequestDto tweetRequestDto) {

        Credentials credentials = credentialsMapper.dtoToEntity(tweetRequestDto.getCredentials());
        Optional<User> optionalUser = userRepository.findByCredentials(credentials);
        if (!optionalUser.isPresent() || optionalUser.get().isDeleted()) {
            throw new NotAuthorizedException("Not a authorized author");
        }
        Optional<Tweet> tweet = tweetRepository.findById(id);
        if (!tweet.isPresent() || tweet.get().isDeleted()) {
            throw new NotFoundException("Tweet does not exist with ID:" + id);
        }
        Tweet replyToTweet = tweet.get();
        Tweet newTweetReplying = new Tweet();
        if (tweetRequestDto.getContent() != null && !tweetRequestDto.getContent().isEmpty()) {
            newTweetReplying.setContent(tweetRequestDto.getContent());
        }
        newTweetReplying.setAuthor(optionalUser.get());
        newTweetReplying.setInReplyTo(replyToTweet);

        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(newTweetReplying));
    }

    @Override
    public TweetResponseDto repostTweetById(Long id, CredentialsDto credentialsDto) {
        Optional<Tweet> optionalTweet = tweetRepository.findById(id);
        Credentials credentials = credentialsMapper.dtoToEntity(credentialsDto);
        Optional<User> optionalUser = userRepository.findByCredentials(credentials);

        if (!optionalTweet.isPresent() || optionalTweet.get().isDeleted()) {
            throw new NotFoundException("Tweet not found");
        }

        if (!optionalUser.isPresent() || optionalUser.get().isDeleted()) {
            throw new NotFoundException("User not found");
        }

        try {
            User user = optionalUser.get();
            Tweet selectedTweet = optionalTweet.get();

            user.getCreatedTweets().add(selectedTweet);
            userRepository.saveAndFlush(user);
            return tweetMapper.entityToDto(selectedTweet);
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_MSG);
        }
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

                for (User u : selectedTweet.getLikedByUsers()) {

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
