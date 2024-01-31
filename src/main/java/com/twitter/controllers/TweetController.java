package com.twitter.controllers;


import com.twitter.dtos.ContextDto;
import com.twitter.dtos.HashtagDto;
import com.twitter.dtos.TweetResponseDto;
import com.twitter.dtos.UserResponseDto;
import com.twitter.entities.Tweet;
import com.twitter.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {

    private final TweetService tweetService;

    @GetMapping
    public List<TweetResponseDto> getAllTweets() {
        return tweetService.getAllTweets();
    }

    @GetMapping("/{id}")
    public TweetResponseDto getTweetById(
            @PathVariable Long id
    ) {
        return tweetService.getTweetById(id);
    }

    @GetMapping("/{id}/tags")
    public List<HashtagDto> getTagsByTweetId(
            @PathVariable Long id
    ) {
        return tweetService.getTagsByTweetId(id);
    }

    @GetMapping("/{id}/mentions")
    public List<TweetResponseDto> getMentionsByTweetId(
            @PathVariable Long id
    ) {
        return tweetService.getMentionsByTweetId(id);
    }

    @GetMapping("/{id}/likes")
    public List<UserResponseDto> getLikesByTweetId(
            @PathVariable Long id
    ) {
        return tweetService.getLikesByTweetId(id);
    }

    @GetMapping("/{id}/context")
    public ContextDto getContextByTweetId(
            @PathVariable Long id
    ) {
        return tweetService.getContextByTweetId(id);
    }

    @GetMapping("/{id}/reports")
    public List<TweetResponseDto> getReportsByTweetId(
            @PathVariable Long id
    ) {
        return tweetService.getRepostsByTweetId(id);
    }

    @GetMapping("/{id}/replies")
    public List<TweetResponseDto> getRepliesByTweetId(
            @PathVariable Long id
    ) {
        return tweetService.getRepliesByTweetId(id);
    }
}
