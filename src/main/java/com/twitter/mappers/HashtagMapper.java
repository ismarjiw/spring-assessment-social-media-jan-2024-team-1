package com.twitter.mappers;

import com.twitter.dtos.HashtagDto;
import com.twitter.entities.Hashtag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring")
public interface HashtagMapper {

    HashtagDto entityToDto(Hashtag hashtag);

    List<HashtagDto> entitiesToDtos(List<Hashtag> associatedTags);

}


