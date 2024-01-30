package com.twitter.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@AllArgsConstructor
@Getter
@Setter
public class NotAuthorizedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4635620105208006067L;

    private String message;

}
