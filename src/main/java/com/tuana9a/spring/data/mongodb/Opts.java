package com.tuana9a.spring.data.mongodb;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Opts {
    public static Opts DEFAULT = new Opts();

    public String delimiter;
    public String regexOptions;
    public String inOperatorDelimiter;
    public Set<String> allowedKeys; // TODO: this is manual to much, what about using regex to check allowed keys ?
    public Set<String> ignoredKeys;

    public Opts() {
        this.delimiter = Config.DEFAULT_DELIMITER;
        this.regexOptions = Config.DEFAULT_REGEX_OPTIONS;
        this.inOperatorDelimiter = Config.DEFAULT_IN_OPERATOR_DELIMITER;
        this.allowedKeys = new HashSet<>();
        this.ignoredKeys = new HashSet<>();
    }

    public Opts addAllowKeys(String... keys) {
        this.allowedKeys.addAll(Arrays.asList(keys));
        return this;
    }

    public Opts addIgnoredKeys(String... keys) {
        this.ignoredKeys.addAll(Arrays.asList(keys));
        return this;
    }
}
