package com.tuana9a.spring.data.mongodb;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Getter
public class SortBuilder {
    public static final String REGEX_PATTERN = "(\\w+)=+(1|-1)";

    protected String key;
    protected String order;

    public SortBuilder(String key, String order) {
        this.key = key;
        this.order = order;
    }

    public static boolean isValid(SortBuilder builder) {
        return builder.key != null && builder.order != null;
    }

    public static SortBuilder unknown() {
        return new SortBuilder(null, null);
    }

    public static SortBuilder from(String input) {
        Pattern pattern = Pattern.compile(REGEX_PATTERN);
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find()) {
            String message = input + " not match " + REGEX_PATTERN;
            log.warn("invalid sort: {}", message);
            return SortBuilder.unknown();
        }
        String key = matcher.group(1).trim();
        String order = matcher.group(2).trim();
        return new SortBuilder(key, order);
    }

    public Sort toSort() {
        switch (this.order) {
            case "1":
                return Sort.by(Sort.Direction.ASC, this.key);
            case "-1":
                return Sort.by(Sort.Direction.DESC, this.key);
            default:
                return Sort.unsorted();
        }
    }

    // chain with existing sort
    public Sort toSort(Sort sort) {
        switch (this.order) {
            case "1":
                return sort.and(Sort.by(Sort.Direction.ASC, this.key));
            case "-1":
                return sort.and(Sort.by(Sort.Direction.DESC, this.key));
            default:
                log.warn("sort operation not supported: {}", this.order);
                return sort;
        }
    }
}
