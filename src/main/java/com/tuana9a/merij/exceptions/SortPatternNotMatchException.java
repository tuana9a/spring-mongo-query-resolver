package com.tuana9a.merij.exceptions;

public class SortPatternNotMatchException extends MerijException {
    public SortPatternNotMatchException(String message) {
        super("query pattern not match " + message);
    }
}
