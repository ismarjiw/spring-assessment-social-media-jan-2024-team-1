package com.twitter.dtos;

import com.twitter.entities.Tweet;

import java.util.List;

public class ContextDto {
    private Tweet target;
    private List<Tweet> before;

    private List<Tweet> after;
}
