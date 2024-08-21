package com.tuana9a.spring.mongo.qrisolver.resolvers;

import com.tuana9a.spring.mongo.qrisolver.configs.Config;
import com.tuana9a.spring.mongo.qrisolver.configs.SupportedOperator;
import com.tuana9a.spring.mongo.qrisolver.errors.Error;
import com.tuana9a.spring.mongo.qrisolver.errors.InvalidPartError;
import com.tuana9a.spring.mongo.qrisolver.payloads.CriteriaPart;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CriteriaPartResolver {
    public static CriteriaPart resolve(String input) throws Error {
        Pattern pattern = Pattern.compile(Config.CRITERIA_PART_REGEX_PATTERN);
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find()) {
            throw new InvalidPartError(input, "not match " + Config.CRITERIA_PART_REGEX_PATTERN);
        }
        String key = matcher.group(1).trim();
        String op = matcher.group(2).trim();
        String rawValue = matcher.group(3);
        if (op.equals(SupportedOperator.IN)) {
            ArrayList<Object> value = new ArrayList<>();
            for (String x : rawValue.split(Config.LIST_VALUE_DELIMITER)) {
                value.add(ValueResolver.resolve(x.trim()));
            }
            return new CriteriaPart(key, op, value);
        }
        Object value = ValueResolver.resolve(rawValue.trim());
        return new CriteriaPart(key, op, value);
    }
}
