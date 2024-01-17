package com.tuana9a.spring.data.mongodb;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Slf4j
@Getter
public class CriteriaBuilder {
    public static final String REGEX_PATTERN = "(\\w+)(==|>=|<=|!=|\\*=|@=|>|<)(.*)";

    protected final String key;
    protected final Object value;
    protected final String operator;
    protected final Opts opts;
    protected final List<CriteriaBuilder> chains;

    public CriteriaBuilder(String key, String operator, Object value, Opts opts) {
        if (isNull(opts)) throw new IllegalArgumentException("opts is null");
        this.key = key;
        this.operator = operator;
        this.value = value;
        this.chains = new LinkedList<>();
        this.opts = opts;
    }

    public static boolean isValid(CriteriaBuilder c) {
        return !isNull(c) && !isNull(c.key) && !isNull(c.operator);
    }

    public static CriteriaBuilder unknown() {
        return new CriteriaBuilder(null, null, null, Opts.DEFAULT);
    }

    public static CriteriaBuilder from(String input) {
        return from(input, Opts.DEFAULT);
    }

    public static CriteriaBuilder from(String input, Opts opts) {
        Pattern pattern = Pattern.compile(REGEX_PATTERN);
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find()) {
            String message = input + " not match " + REGEX_PATTERN;
            log.warn("invalid query: {}", message);
            return CriteriaBuilder.unknown();
        }
        String key = matcher.group(1).trim();
        String op = matcher.group(2).trim();
        String stringValue = matcher.group(3);
        if (op.equals("@=")) {
            List<?> value = Arrays.stream(stringValue.split(opts.inOperatorDelimiter)).map(x -> Utils.resolve(x.trim())).collect(Collectors.toList());
            return new CriteriaBuilder(key, op, value, opts);
        }
        Object value = Utils.resolve(stringValue.trim());
        return new CriteriaBuilder(key, op, value, opts);
    }

    public Criteria toCriteria() {
        return toCriteria(new Criteria());
    }

    public Criteria toCriteria(Criteria criteria) {
        switch (this.operator) {
            case "==":
                criteria = criteria.and(key).is(value);
                break;
            case ">":
                criteria = criteria.and(key).gt(value);
                break;
            case "<":
                criteria = criteria.and(key).lt(value);
                break;
            case ">=":
                criteria = criteria.and(key).gte(value);
                break;
            case "<=":
                criteria = criteria.and(key).lte(value);
                break;
            case "!=":
                criteria = criteria.and(key).ne(value);
                break;
            case "*=":
                criteria = criteria.and(key).regex(String.valueOf(value), opts.regexOptions);
                break;
            case "@=":
                criteria = criteria.and(key).in((List<?>) value);
                break;
            default:
                log.warn("criteria operation not supported: {}", this.operator);
                break;
        }
        for (CriteriaBuilder chain : chains) {
            switch (chain.operator) {
                case "==":
                    criteria = criteria.is(chain.value);
                    break;
                case ">":
                    criteria = criteria.gt(chain.value);
                    break;
                case "<":
                    criteria = criteria.lt(chain.value);
                    break;
                case ">=":
                    criteria = criteria.gte(chain.value);
                    break;
                case "<=":
                    criteria = criteria.lte(chain.value);
                    break;
                case "!=":
                    criteria = criteria.ne(chain.value);
                    break;
                case "*=":
                    criteria = criteria.regex(String.valueOf(chain.value), opts.regexOptions);
                    break;
                case "@=":
                    criteria = criteria.in((List<?>) chain.value);
                    break;
                default:
                    log.warn("criteria operation not supported: {}", chain.operator);
                    break;
            }
        }
        return criteria;
    }

    public CriteriaBuilder chain(CriteriaBuilder chain) {
        this.chains.add(chain);
        return this;
    }
}
