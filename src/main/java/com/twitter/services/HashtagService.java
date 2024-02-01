package com.twitter.services;

import com.twitter.dtos.HashtagDto;

import java.util.List;

public interface HashtagService {

    List<HashtagDto> getAllTags();
    HashtagDto getRandomTag();

}
