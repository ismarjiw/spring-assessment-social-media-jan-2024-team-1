package com.twitter.dtos;

import com.twitter.entities.Tweet;

import java.util.List;

public class contextDto {
    private Tweet target;
    private List<TweetResponseDto> before;

    private List<TweetResponseDto> after;
}
