package com.twitter.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@AllArgsConstructor
@Getter
@Setter
public class BadRequestException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3421260396561246763L;

    private String message;

}
