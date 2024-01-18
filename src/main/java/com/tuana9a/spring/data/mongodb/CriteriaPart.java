package com.tuana9a.spring.data.mongodb;

import lombok.Getter;
import lombok.Setter;

public class CriteriaPart {
    public static final String REGEX_PATTERN = "(\\w+)(==|>=|<=|!=|\\*=|@=|>|<)(.*)";

    private @Getter @Setter String key;
    private @Getter @Setter Object value;
    private @Getter @Setter String operator;
    public boolean isError;

    public CriteriaPart(String key, String operator, Object value) {
        this.key = key;
        this.operator = operator;
        this.value = value;
    }

    public static CriteriaPart error() {
        CriteriaPart part = new CriteriaPart(null, null, null);
        part.isError = true;
        return part;
    }
}
