package com.tuana9a.merij.exceptions;

public class CriteriaOperationNotSupported extends MerijException {
    public CriteriaOperationNotSupported(String op) {
        super("criteria operation not supported: " + op);
    }
}
