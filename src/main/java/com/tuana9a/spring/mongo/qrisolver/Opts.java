package com.tuana9a.spring.mongo.qrisolver;

public class Opts {
    public static Opts DEFAULT = new Opts();

    public String delimiter;
    public String regexOptions;
    public String inOperatorDelimiter;

    public Opts() {
        this.delimiter = Config.DELIMITER;
        this.regexOptions = Config.REGEX_OPTIONS;
        this.inOperatorDelimiter = Config.LIST_VALUE_DELIMITER;
    }
}
