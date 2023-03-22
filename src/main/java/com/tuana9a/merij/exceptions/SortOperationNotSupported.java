package com.tuana9a.merij.exceptions;

public class SortOperationNotSupported extends MerijException {
    public SortOperationNotSupported(String message) {
        super("sort operation not supported: " + message);
    }
}
