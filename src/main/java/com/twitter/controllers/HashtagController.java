package com.twitter.controllers;

import com.twitter.dtos.HashtagDto;
import com.twitter.dtos.TweetResponseDto;
import com.twitter.services.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class HashtagController {
private final HashtagService hashtagService;

@GetMapping
public List<HashtagDto> getAllTags(){
    return hashtagService.getAllTags();
}
//@GetMapping("/randomTag")
//public HashtagDto getRandomTag(){
//return hashtagService.getRandomTag();
//}
    @GetMapping("/{label}")
    public List<TweetResponseDto> getTagLabel(@PathVariable String label){
        return hashtagService.getTagLabel(label);
    }
}




