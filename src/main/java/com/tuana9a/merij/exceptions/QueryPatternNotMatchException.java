package com.tuana9a.merij.exceptions;

public class QueryPatternNotMatchException extends MerijException {
    public QueryPatternNotMatchException(String message) {
        super("query pattern not match " + message);
    }
}
