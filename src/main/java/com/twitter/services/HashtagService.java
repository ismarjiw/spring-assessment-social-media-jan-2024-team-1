package com.twitter.services;

import com.twitter.dtos.HashtagDto;
import com.twitter.dtos.TweetResponseDto;

import java.util.List;

public interface HashtagService {

    List<HashtagDto> getAllTags();
    HashtagDto getRandomTag();

    List<TweetResponseDto> getTagLabel(String label);
}
