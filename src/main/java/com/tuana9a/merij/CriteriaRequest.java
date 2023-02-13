package com.tuana9a.merij;

import com.tuana9a.merij.exceptions.CriteriaOperationNotSupported;
import com.tuana9a.merij.exceptions.CriteriaQueryLogicException;
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

    private CriteriaRequest() {
        this.regexOptions = "";
    }

    public static CriteriaRequest resolve(String input) {
        try {
            Pattern pattern = Pattern.compile("(\\w+\\s*)(==|>=|<=|!=|\\*=|#=|>|<)(.*)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (!matcher.find()) {
                return new CriteriaRequest(); // success = false
            }
            String key = matcher.group(1).trim();
            String op = matcher.group(2).trim();
            String value = matcher.group(3);
            if (op.equals("#=")) {
                return new CriteriaRequest().key(key).op(op).value(Arrays.stream(value.split(";")).map(x -> StringUtils.auto(x.trim())).collect(Collectors.toList()));
            }
            return new CriteriaRequest().key(key).op(op).value(StringUtils.auto(value.trim()));
        } catch (IndexOutOfBoundsException ignored) {
        }
        return new CriteriaRequest();
    }

    public static List<CriteriaRequest> reduce(List<CriteriaRequest> criteriaRequestList) {
        Map<String, CriteriaRequest> criteriaRequestHashMap = new HashMap<>();
        for (CriteriaRequest entry : criteriaRequestList) {
            String key = entry.key;
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
        return new CriteriaRequest().key(key).op(op).value(value);
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

    public boolean isValid() {
        return this.key != null && this.operator != null && this.value != null;
    }

    public Criteria init() throws CriteriaOperationNotSupported, CriteriaQueryLogicException {
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
            case "#=":
                criteria = Criteria.where(key).in((List) value);
                break;
            default:
                throw new CriteriaOperationNotSupported(this.operator);
        }
        CriteriaRequest next = this.next;
        Criteria chain = criteria;
        while (next != null) {
            switch (next.operator) {
                case "==":
                    // will throw db logic error
                    throw new CriteriaQueryLogicException("next.operator is '==' recheck your query logic");
                case ">":
                    chain = criteria.gt(next.value);
                    break;
                case "<":
                    chain = criteria.lt(next.value);
                    break;
                case ">=":
                    chain = criteria.gte(next.value);
                    break;
                case "<=":
                    chain = criteria.lte(next.value);
                    break;
                case "!=":
                    chain = criteria.ne(next.value);
                    break;
                case "*=":
                    chain = criteria.regex(String.valueOf(next.value), regexOptions);
                    break;
                case "#=":
                    throw new CriteriaQueryLogicException("next.operator is '#=' recheck your query logic");
                default:
                    throw new CriteriaOperationNotSupported(next.operator);
            }
            next = next.next;
        }
        return chain;
    }

    public Criteria chain(Criteria first) throws CriteriaOperationNotSupported, CriteriaQueryLogicException {
        Criteria firstChain;
        switch (this.operator) {
            case "==":
                firstChain = first.and(key).is(value);
                break;
            case ">":
                firstChain = first.and(key).gt(value);
                break;
            case "<":
                firstChain = first.and(key).lt(value);
                break;
            case ">=":
                firstChain = first.and(key).gte(value);
                break;
            case "<=":
                firstChain = first.and(key).lte(value);
                break;
            case "!=":
                firstChain = first.and(key).ne(value);
                break;
            case "*=":
                firstChain = first.and(key).regex(String.valueOf(value), regexOptions);
                break;
            case "#=":
                firstChain = first.and(key).in((List) value);
                break;
            default:
                throw new CriteriaOperationNotSupported(this.operator);
        }
        CriteriaRequest next = this.next;
        Criteria nextChain = firstChain;
        while (next != null) {
            switch (next.operator) {
                case "==":
                    // will throw db logic error
                    throw new CriteriaQueryLogicException("next.operator is '==' recheck your query logic");
                case ">":
                    nextChain.gt(next.value);
                    break;
                case "<":
                    nextChain.lt(next.value);
                    break;
                case ">=":
                    nextChain.gte(next.value);
                    break;
                case "<=":
                    nextChain.lte(next.value);
                    break;
                case "!=":
                    nextChain.ne(next.value);
                    break;
                case "*=":
                    nextChain.regex(String.valueOf(next.value), regexOptions);
                    break;
                case "#=":
                    throw new CriteriaQueryLogicException("next.operator is '#=' recheck your query logic");
                default:
                    throw new CriteriaOperationNotSupported(next.operator);
            }
            next = next.next;
        }
        return nextChain;
    }

    public CriteriaRequest next(CriteriaRequest next) {
        if (this.next != null) {
            return this.next.next(next);
        }
        this.next = next;
        return next;
    }
}
