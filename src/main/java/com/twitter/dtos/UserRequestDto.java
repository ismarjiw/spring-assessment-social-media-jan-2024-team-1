package com.twitter.dtos;

import com.twitter.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserRequestDto {

    private User.Credentials credentials;

    private User.Profile profile;

}
