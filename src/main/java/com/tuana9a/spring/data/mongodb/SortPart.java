package com.tuana9a.spring.data.mongodb;

import lombok.Getter;
import lombok.Setter;

public class SortPart {
    public static final String REGEX_PATTERN = "(\\w+)=+(1|-1)";

    private @Getter @Setter String key;
    private @Getter @Setter String order;
    public boolean isError = false;

    public SortPart(String key, String order) {
        this.key = key;
        this.order = order;
    }

    public static SortPart error() {
        SortPart part = new SortPart(null, null);
        part.isError = true;
        return part;
    }
}
