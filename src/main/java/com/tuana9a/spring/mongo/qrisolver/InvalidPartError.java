package com.tuana9a.spring.mongo.qrisolver;

import lombok.Getter;

public class InvalidPartError extends Error {
    @Getter
    private String part;
    @Getter
    private String reason;

    public InvalidPartError(String part, String reason) {
        super("InvalidPart: " + part + " " + reason);
        this.part = part;
        this.reason = reason;
    }
}