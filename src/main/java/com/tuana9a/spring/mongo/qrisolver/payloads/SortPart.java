package com.tuana9a.spring.mongo.qrisolver.payloads;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SortPart {
    private String key;
    private String order;

    public SortPart(String key, String order) {
        this.key = key;
        this.order = order;
    }
}
