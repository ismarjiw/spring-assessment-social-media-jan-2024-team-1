package com.twitter.dtos;

import com.twitter.entities.Tweet;
import com.twitter.entities.User;

import java.sql.Timestamp;

public class TweetResponseDto {
    private Long id;
    private User author;

    private Timestamp posted;

    private String content;

    private Tweet inReplyTO;

    private Tweet repostOf;

}
