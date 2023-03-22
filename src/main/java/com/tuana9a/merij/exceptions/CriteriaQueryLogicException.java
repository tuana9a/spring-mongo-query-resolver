package com.tuana9a.merij.exceptions;

public class CriteriaQueryLogicException extends MerijException {
    public CriteriaQueryLogicException(String message) {
        super("criteria query logic error: " + message);
    }
}
