package com.twitter.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@AllArgsConstructor
@Getter
@Setter
public class NotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3898840660074223864L;

    private String message;
}
