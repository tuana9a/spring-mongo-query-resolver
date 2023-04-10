package com.tuana9a.merij.exceptions;

public class CriteriaLogicException extends MerijException {
    public CriteriaLogicException(String message) {
        super("criteria logic error: " + message);
    }
}
