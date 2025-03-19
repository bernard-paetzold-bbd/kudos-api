package com.bbdgrads.kudos_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User cannot send a message to themselves")
public class SelfMessageException extends RuntimeException {
    public SelfMessageException(Long userId) {
        super("User " + Long.toString(userId) + " cannot send a message to themselves");
    }
}
