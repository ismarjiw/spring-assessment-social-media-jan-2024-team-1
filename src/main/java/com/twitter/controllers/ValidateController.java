package com.twitter.controllers;

import com.twitter.services.HashtagService;
import com.twitter.services.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/validate")
public class ValidateController {
    private final ValidateService validateService;
    @GetMapping("/tag/exists/{label}")
    public boolean checkHashtagExists(@PathVariable String label){
    return validateService.checkHashtagExists(label);
    }
    @GetMapping("/username/exists/@{username}")
    public boolean checkUserExists(@PathVariable String username){
        return  validateService.checkUserExists(username);
    }
    @GetMapping("/username/available/@{username}")
    public  boolean checkUsernameAvailable(@PathVariable String username){
        return  validateService.checkUsernameAvailable(username);
    }

}
