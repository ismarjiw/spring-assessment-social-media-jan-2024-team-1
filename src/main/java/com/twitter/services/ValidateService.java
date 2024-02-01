package com.twitter.services;


public interface ValidateService {

    boolean checkHashtagExists(String label);

    boolean checkUserExists(String username);

    boolean checkUsernameAvailable(String username);
}
