package com.tuana9a.spring.mongo.qrisolver.configs;

public class Config {
    public static String REGEX_OPTIONS = "";
    public static String DELIMITER = ",";
    public static String LIST_VALUE_DELIMITER = ";";
    public static String CRITERIA_PART_REGEX_PATTERN = "(\\w+)(==|>=|<=|!=|\\*=|@=|>|<)(.*)";
    public static String SORT_PART_REGEX_PATTERN = "(\\w+)=+(1|-1)";
}
