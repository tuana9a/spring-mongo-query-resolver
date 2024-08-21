package com.tuana9a.spring.mongo.qrisolver.resolvers;

import com.tuana9a.spring.mongo.qrisolver.configs.Config;
import com.tuana9a.spring.mongo.qrisolver.errors.Error;
import com.tuana9a.spring.mongo.qrisolver.errors.InvalidPartError;
import com.tuana9a.spring.mongo.qrisolver.payloads.SortPart;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SortPartResolver {
    public static SortPart resolve(String input) throws Error {
        Pattern pattern = Pattern.compile(Config.SORT_PART_REGEX_PATTERN);
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find()) {
            throw new InvalidPartError(input, "not match " + Config.SORT_PART_REGEX_PATTERN);
        }
        String key = matcher.group(1).trim();
        String order = matcher.group(2).trim();
        return new SortPart(key, order);
    }
}
