package com.twitter.mappers;

import com.twitter.dtos.TweetRequestDto;
import com.twitter.dtos.TweetResponseDto;
import com.twitter.entities.Tweet;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring", uses = {UserMapper.class})
public interface TweetMapper {

    Tweet requestDtoToEntity(TweetRequestDto tweetRequestDto);
    TweetResponseDto entityToDto(Tweet entity);
    List<TweetResponseDto> entitiesToDtos(List<Tweet> entities);
}
