package com.tuana9a.spring.mongo.qrisolver.configs;

public class QueryResolverConfig {
    public static String REGEX_OPTIONS = "";
    public static String DELIMITER = ",";
    public static String LIST_VALUE_DELIMITER = ";";
    public static final String CRITERIA_PART_REGEX_PATTERN = "(\\w+)(==|>=|<=|!=|\\*=|@=|>|<)(.*)";
    public static final String SORT_PART_REGEX_PATTERN = "(\\w+)=+(1|-1)";

    public static class Operator {
        public static String EQ = "==";
        public static String NE = "!=";
        public static String GT = ">";
        public static String LT = "<";
        public static String GTE = ">=";
        public static String LTE = "<=";
        public static String REGEX = "*=";
        public static String IN = "@=";
        public static String ASC = "1";
        public static String DESC = "-1";
    }
}
