package com.tuana9a.spring.data.mongodb;

import lombok.Getter;

public class InvalidOrderError extends Error {
    @Getter
    private String op;

    public InvalidOrderError(String op) {
        super("InvalidOrder: " + op);
        this.op = op;
    }
}