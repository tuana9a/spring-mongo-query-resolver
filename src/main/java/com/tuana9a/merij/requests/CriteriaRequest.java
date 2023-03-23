package com.tuana9a.merij.requests;

import com.tuana9a.merij.exceptions.CriteriaOperationNotSupported;
import com.tuana9a.merij.exceptions.CriteriaQueryLogicException;
import com.tuana9a.merij.exceptions.QueryPatternNotMatchException;
import com.tuana9a.merij.utils.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CriteriaRequest {
    private String key;
    private Object value;
    private String operator;
    private CriteriaRequest next;
    private String regexOptions;
    public static String DEFAULT_REGEX_OPTIONS = "";

    private CriteriaRequest() {
        this.regexOptions = DEFAULT_REGEX_OPTIONS;
    }

    public static CriteriaRequest resolve(String input) throws QueryPatternNotMatchException {
        Pattern pattern = Pattern.compile("(\\w+\\s*)(==|>=|<=|!=|\\*=|@=|>|<)(.*)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find()) {
            throw new QueryPatternNotMatchException(input);
        }
        String key = matcher.group(1).trim();
        String op = matcher.group(2).trim();
        String stringValue = matcher.group(3);
        if (op.equals("@=")) {
            return new CriteriaRequest()
                    .key(key)
                    .op(op)
                    .value(Arrays.stream(stringValue.split(";")).map(x -> StringUtils.auto(x.trim())).collect(Collectors.toList()));
        }
        Object parsedValue = StringUtils.auto(stringValue.trim());
        return new CriteriaRequest().key(key).op(op).value(parsedValue);
    }

    public static List<CriteriaRequest> reduce(List<CriteriaRequest> criteriaRequestList) {
        Map<String, CriteriaRequest> criteriaRequestHashMap = new HashMap<>();
        for (CriteriaRequest entry : criteriaRequestList) {
            String key = entry.key();
            CriteriaRequest existOne = criteriaRequestHashMap.get(key);
            if (existOne == null) {
                criteriaRequestHashMap.put(key, entry);
            } else {
                existOne.next(entry);
            }
        }
        return new LinkedList<>(criteriaRequestHashMap.values());
    }

    public static CriteriaRequest from(String key, String op, Object value) {
        return new CriteriaRequest()
                .key(key)
                .op(op)
                .value(value);
    }

    public String key() {
        return this.key;
    }

    public CriteriaRequest key(String key) {
        this.key = key;
        return this;
    }

    public String op() {
        return this.operator;
    }

    public CriteriaRequest op(String operator) {
        this.operator = operator;
        return this;
    }

    public CriteriaRequest value(Object value) {
        this.value = value;
        return this;
    }

    public CriteriaRequest withRegexOptions(String regexOptions) {
        this.regexOptions = regexOptions;
        return this;
    }

    public Criteria toCriteria() throws CriteriaOperationNotSupported, CriteriaQueryLogicException {
        Criteria criteria;
        switch (this.operator) {
            case "==":
                criteria = Criteria.where(key).is(value);
                break;
            case ">":
                criteria = Criteria.where(key).gt(value);
                break;
            case "<":
                criteria = Criteria.where(key).lt(value);
                break;
            case ">=":
                criteria = Criteria.where(key).gte(value);
                break;
            case "<=":
                criteria = Criteria.where(key).lte(value);
                break;
            case "!=":
                criteria = Criteria.where(key).ne(value);
                break;
            case "*=":
                criteria = Criteria.where(key).regex(String.valueOf(value), regexOptions);
                break;
            case "@=":
                criteria = Criteria.where(key).in((List) value);
                break;
            default:
                throw new CriteriaOperationNotSupported(this.operator);
        }
        if (this.next != null) {
            criteria = this.next.toCriteria(criteria);
        }
        return criteria;
    }

    public Criteria toCriteria(Criteria first) throws CriteriaOperationNotSupported, CriteriaQueryLogicException {
        Criteria criteria;
        switch (this.operator) {
            case "==":
                // will throw db logic error
                throw new CriteriaQueryLogicException("next.operator is '==' recheck your query logic");
            case ">":
                criteria = first.gt(this.value);
                break;
            case "<":
                criteria = first.lt(this.value);
                break;
            case ">=":
                criteria = first.gte(this.value);
                break;
            case "<=":
                criteria = first.lte(this.value);
                break;
            case "!=":
                criteria = first.ne(this.value);
                break;
            case "*=":
                criteria = first.regex(String.valueOf(this.value), regexOptions);
                break;
            case "@=":
                throw new CriteriaQueryLogicException("next.operator is '@=' recheck your query logic");
            default:
                throw new CriteriaOperationNotSupported(this.operator);
        }
        if (this.next != null) {
            criteria = this.next.toCriteria(criteria);
        }
        return criteria;
    }

    public Criteria and(Criteria first) throws CriteriaOperationNotSupported, CriteriaQueryLogicException {
        Criteria criteria;
        switch (this.operator) {
            case "==":
                criteria = first.and(key).is(value);
                break;
            case ">":
                criteria = first.and(key).gt(value);
                break;
            case "<":
                criteria = first.and(key).lt(value);
                break;
            case ">=":
                criteria = first.and(key).gte(value);
                break;
            case "<=":
                criteria = first.and(key).lte(value);
                break;
            case "!=":
                criteria = first.and(key).ne(value);
                break;
            case "*=":
                criteria = first.and(key).regex(String.valueOf(value), regexOptions);
                break;
            case "@=":
                criteria = first.and(key).in((List) value);
                break;
            default:
                throw new CriteriaOperationNotSupported(this.operator);
        }
        if (this.next != null) {
            criteria = this.next.toCriteria(criteria);
        }
        return criteria;
    }

    public CriteriaRequest next(CriteriaRequest next) {
        if (this.next != null) {
            return this.next.next(next);
        }
        this.next = next;
        return next;
    }
}
