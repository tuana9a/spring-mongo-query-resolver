package com.tuana9a.spring.mongo.qrisolver.errors;

import lombok.Getter;

@Getter
public class InvalidPartError extends Error {
    private final String part;
    private final String reason;

    public InvalidPartError(String part, String reason) {
        super("InvalidPart: " + part + " " + reason);
        this.part = part;
        this.reason = reason;
    }
}