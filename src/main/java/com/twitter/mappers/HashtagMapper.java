package com.twitter.mappers;

import com.twitter.dtos.HashtagDto;

import com.twitter.entities.Hashtag;

import com.twitter.dtos.TweetRequestDto;
import com.twitter.dtos.TweetResponseDto;
import com.twitter.entities.Hashtag;
import com.twitter.entities.Tweet;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring")
public interface HashtagMapper {


    List<HashtagDto> entitiesToDtos(List<Hashtag> all);

    HashtagDto entityToDto(Hashtag hashtag);

    List<HashtagDto> entitiesToDtos(List<Hashtag> associatedTags);

}


