package com.tuana9a.spring.mongo.qrisolver.errors;

import lombok.Getter;

@Getter
public class InvalidOperatorError extends Error {
    private final String op;

    public InvalidOperatorError(String op) {
        super("InvalidOperator: " + op);
        this.op = op;
    }
}