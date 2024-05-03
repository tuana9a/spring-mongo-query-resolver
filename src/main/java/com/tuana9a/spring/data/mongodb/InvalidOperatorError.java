package com.tuana9a.spring.data.mongodb;

import lombok.Getter;

public class InvalidOperatorError extends Error {
    @Getter
    private String op;

    public InvalidOperatorError(String op) {
        super("InvalidOperator: " + op);
        this.op = op;
    }
}