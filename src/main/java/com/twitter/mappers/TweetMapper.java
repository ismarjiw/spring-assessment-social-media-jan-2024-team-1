package com.twitter.mappers;

import com.twitter.dtos.TweetResponseDto;
import com.twitter.entities.Tweet;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring", uses = {UserMapper.class})
public interface TweetMapper {
    List<TweetResponseDto> entitiesToDtos(List<Tweet> createdTweets);
}
