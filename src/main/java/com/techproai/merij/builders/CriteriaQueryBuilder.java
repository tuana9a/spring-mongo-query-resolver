package com.techproai.merij.builders;

import com.techproai.merij.exceptions.CriteriaOperationNotSupported;
import com.techproai.merij.exceptions.CriteriaQueryLogicException;
import com.techproai.merij.utils.StringUtils;
import lombok.Getter;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CriteriaQueryBuilder {
    @Getter
    private String key;
    @Getter
    private Object value;
    @Getter
    private String operator;
    private CriteriaQueryBuilder other;
    private String regexOptions;

    public CriteriaQueryBuilder() {
        this.regexOptions = "";
    }

    public static CriteriaQueryBuilder resolve(String input) {
        try {
            Pattern pattern = Pattern.compile("(\\w+\\s*)(==|>=|<=|!=|\\*=|>|<)(.*)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (!matcher.find()) {
                return new CriteriaQueryBuilder(); // success = false
            }
            return new CriteriaQueryBuilder()
                    .key(matcher.group(1).trim())
                    .op(matcher.group(2).trim())
                    .value(StringUtils.auto(matcher.group(3).trim()));
        } catch (IndexOutOfBoundsException ignored) {
        }
        return new CriteriaQueryBuilder();
    }

    public static List<CriteriaQueryBuilder> reduce(List<CriteriaQueryBuilder> criteriaQueryBuilderList) {
        Map<String, CriteriaQueryBuilder> queryBuilderHashMap = new HashMap<>();
        for (CriteriaQueryBuilder entry : criteriaQueryBuilderList) {
            String key = entry.getKey();
            CriteriaQueryBuilder existOne = queryBuilderHashMap.get(key);
            if (existOne == null) {
                queryBuilderHashMap.put(key, entry);
            } else {
                existOne.other(entry);
            }
        }
        return new LinkedList<>(queryBuilderHashMap.values());
    }

    public CriteriaQueryBuilder key(String key) {
        this.key = key;
        return this;
    }

    public CriteriaQueryBuilder op(String operator) {
        this.operator = operator;
        return this;
    }

    public CriteriaQueryBuilder value(Object value) {
        this.value = value;
        return this;
    }

    public CriteriaQueryBuilder withRegexOptions(String regexOptions) {
        this.regexOptions = regexOptions;
        return this;
    }

    public boolean isValid() {
        return this.key != null && this.operator != null && this.value != null;
    }

    public Criteria first() throws CriteriaOperationNotSupported, CriteriaQueryLogicException {
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
            default:
                throw new CriteriaOperationNotSupported(this.operator);
        }
        CriteriaQueryBuilder other = this.other;
        Criteria chain = criteria;
        while (other != null) {
            switch (other.operator) {
                case "==":
                    // criteria.is(other.value); // will throw mongo error :))
                    throw new CriteriaQueryLogicException("other.operator is '==' recheck your query logic");
                case ">":
                    chain = criteria.gt(other.value);
                    break;
                case "<":
                    chain = criteria.lt(other.value);
                    break;
                case ">=":
                    chain = criteria.gte(other.value);
                    break;
                case "<=":
                    chain = criteria.lte(other.value);
                    break;
                case "!=":
                    chain = criteria.ne(other.value);
                    break;
                case "*=":
                    chain = criteria.regex(String.valueOf(other.value), regexOptions);
                    break;
                default:
                    throw new CriteriaOperationNotSupported(other.operator);
            }
            other = other.other;
        }
        return chain;
    }

    public Criteria chain(Criteria first) throws CriteriaOperationNotSupported, CriteriaQueryLogicException {
        Criteria chain0;
        switch (this.operator) {
            case "==":
                chain0 = first.and(key).is(value);
                break;
            case ">":
                chain0 = first.and(key).gt(value);
                break;
            case "<":
                chain0 = first.and(key).lt(value);
                break;
            case ">=":
                chain0 = first.and(key).gte(value);
                break;
            case "<=":
                chain0 = first.and(key).lte(value);
                break;
            case "!=":
                chain0 = first.and(key).ne(value);
                break;
            case "*=":
                chain0 = first.and(key).regex(String.valueOf(value), regexOptions);
                break;
            default:
                throw new CriteriaOperationNotSupported(this.operator);
        }
        CriteriaQueryBuilder other = this.other;
        Criteria chain1 = chain0;
        while (other != null) {
            switch (other.operator) {
                case "==":
                    // first.is(other.value); // will throw mongo db error
                    throw new CriteriaQueryLogicException("other.operator is '==' recheck your query logic");
                case ">":
                    chain1.gt(other.value);
                    break;
                case "<":
                    chain1.lt(other.value);
                    break;
                case ">=":
                    chain1.gte(other.value);
                    break;
                case "<=":
                    chain1.lte(other.value);
                    break;
                case "!=":
                    chain1.ne(other.value);
                    break;
                case "*=":
                    chain1.regex(String.valueOf(other.value), regexOptions);
                    break;
                default:
                    throw new CriteriaOperationNotSupported(other.operator);
            }
            other = other.other;
        }
        return chain1;
    }

    public CriteriaQueryBuilder other(CriteriaQueryBuilder other) {
        if (this.other != null) {
            return this.other.other(other);
        }
        this.other = other;
        return other;
    }
}
