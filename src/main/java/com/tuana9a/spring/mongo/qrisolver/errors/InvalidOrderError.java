package com.tuana9a.spring.mongo.qrisolver.errors;

import lombok.Getter;

@Getter
public class InvalidOrderError extends Error {
    private final String op;

    public InvalidOrderError(String op) {
        super("InvalidOrder: " + op);
        this.op = op;
    }
}