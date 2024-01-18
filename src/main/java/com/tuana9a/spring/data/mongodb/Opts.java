package com.tuana9a.spring.data.mongodb;

public class Opts {
    public static Opts DEFAULT = new Opts();

    public String delimiter;
    public String regexOptions;
    public String inOperatorDelimiter;

    public Opts() {
        this.delimiter = Config.DEFAULT_DELIMITER;
        this.regexOptions = Config.DEFAULT_REGEX_OPTIONS;
        this.inOperatorDelimiter = Config.DEFAULT_IN_OPERATOR_DELIMITER;
    }
}
