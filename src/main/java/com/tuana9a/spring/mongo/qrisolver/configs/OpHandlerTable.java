package com.tuana9a.spring.mongo.qrisolver.configs;

import com.tuana9a.spring.mongo.qrisolver.errors.Error;
import com.tuana9a.spring.mongo.qrisolver.errors.InvalidOperatorError;
import com.tuana9a.spring.mongo.qrisolver.payloads.OpHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

public class OpHandlerTable {
    private static final OpHandlerTable INSTANCE = new OpHandlerTable();
    private final Map<String, OpHandler> map;

    public OpHandlerTable() {
        this.map = new HashMap<>();
        this.map.put(SupportedOperator.EQ, new OpHandler(
                (criteria, part) -> criteria.and(part.getKey()).is(part.getValue()),
                (criteria, part) -> criteria.is(part.getValue())
        ));
        this.map.put(SupportedOperator.GT, new OpHandler(
                (criteria, part) -> criteria.and(part.getKey()).gt(part.getValue()),
                (criteria, part) -> criteria.gt(part.getValue())
        ));
        this.map.put(SupportedOperator.LT, new OpHandler(
                (criteria, part) -> criteria.and(part.getKey()).lt(part.getValue()),
                (criteria, part) -> criteria.lt(part.getValue())
        ));
        this.map.put(SupportedOperator.GTE, new OpHandler(
                (criteria, part) -> criteria.and(part.getKey()).gte(part.getValue()),
                (criteria, part) -> criteria.gte(part.getValue())
        ));
        this.map.put(SupportedOperator.LTE, new OpHandler(
                (criteria, part) -> criteria.and(part.getKey()).lte(part.getValue()),
                (criteria, part) -> criteria.lte(part.getValue())
        ));
        this.map.put(SupportedOperator.NE, new OpHandler(
                (criteria, part) -> criteria.and(part.getKey()).ne(part.getValue()),
                (criteria, part) -> criteria.ne(part.getValue())
        ));
        this.map.put(SupportedOperator.IN, new OpHandler(
                (criteria, part) -> criteria.and(part.getKey()).in((List<?>) part.getValue()),
                (criteria, part) -> criteria.in((List<?>) part.getValue())
        ));
        this.map.put(SupportedOperator.REGEX, new OpHandler(
                (criteria, part) -> criteria.and(part.getKey()).regex(String.valueOf(part.getValue()), Config.REGEX_OPTIONS),
                (criteria, part) -> criteria.regex(String.valueOf(part.getValue()), Config.REGEX_OPTIONS)
        ));
    }

    public static OpHandlerTable getInstance() {
        return INSTANCE;
    }

    public OpHandler get(String op) throws Error {
        OpHandler handler = map.get(op);
        if (isNull(handler)) throw new InvalidOperatorError(op);
        return handler;
    }
}
