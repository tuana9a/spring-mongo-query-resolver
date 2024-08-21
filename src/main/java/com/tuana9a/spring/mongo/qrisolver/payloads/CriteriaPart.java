package com.tuana9a.spring.mongo.qrisolver.payloads;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CriteriaPart {
    private String key;
    private Object value;
    private String operator;

    public CriteriaPart(String key, String operator, Object value) {
        this.key = key;
        this.operator = operator;
        this.value = value;
    }
}
