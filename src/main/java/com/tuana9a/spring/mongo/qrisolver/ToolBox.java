package com.tuana9a.spring.mongo.qrisolver;

import com.tuana9a.spring.mongo.qrisolver.configs.QueryResolverConfig;
import com.tuana9a.spring.mongo.qrisolver.errors.Error;
import com.tuana9a.spring.mongo.qrisolver.errors.InvalidOperatorError;
import com.tuana9a.spring.mongo.qrisolver.errors.InvalidOrderError;
import com.tuana9a.spring.mongo.qrisolver.errors.InvalidPartError;
import com.tuana9a.spring.mongo.qrisolver.payloads.CriteriaPart;
import com.tuana9a.spring.mongo.qrisolver.payloads.SortPart;
import com.tuana9a.spring.mongo.qrisolver.utils.Utils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class ToolBox {
    public static CriteriaPart buildCriteriaPart(String input) throws Error {
        Pattern pattern = Pattern.compile(QueryResolverConfig.CRITERIA_PART_REGEX_PATTERN);
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find()) {
            throw new InvalidPartError(input, "not match " + QueryResolverConfig.CRITERIA_PART_REGEX_PATTERN);
        }
        String key = matcher.group(1).trim();
        String op = matcher.group(2).trim();
        String stringValue = matcher.group(3);
        if (op.equals(QueryResolverConfig.Operator.IN)) {
            List<?> value = Arrays.stream(stringValue.split(QueryResolverConfig.LIST_VALUE_DELIMITER))
                    .map(x -> Utils.resolveValue(x.trim()))
                    .collect(Collectors.toList());
            return new CriteriaPart(key, op, value);
        }
        Object value = Utils.resolveValue(stringValue.trim());
        return new CriteriaPart(key, op, value);
    }

    public static SortPart buildSortPart(String input) throws Error {
        Pattern pattern = Pattern.compile(QueryResolverConfig.SORT_PART_REGEX_PATTERN);
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find()) {
            throw new InvalidPartError(input, "not match " + QueryResolverConfig.SORT_PART_REGEX_PATTERN);
        }
        String key = matcher.group(1).trim();
        String order = matcher.group(2).trim();
        return new SortPart(key, order);
    }

    public static Criteria buildCriteria(Collection<CriteriaPart> parts) throws Error {
        Map<String, Queue<CriteriaPart>> table = new HashMap<>();
        for (CriteriaPart part : parts) {
            String key = part.getKey();
            Queue<CriteriaPart> existed = table.get(key);
            if (!isNull(existed)) {
                existed.offer(part);
                continue;
            }
            Queue<CriteriaPart> queue = new LinkedList<>();
            queue.add(part);
            table.put(key, queue);
        }
        Criteria criteria = new Criteria();
        for (Queue<CriteriaPart> queue : table.values()) {
            CriteriaPart firstOne = queue.poll();
            String key = firstOne.getKey();
            String op = firstOne.getOperator();
            // TODO: table of processor: HashMap<String, Function> -> Criteria
            if (op.equals(QueryResolverConfig.Operator.EQ)) {
                criteria = criteria.and(key).is(firstOne.getValue());
            } else if (op.equals(QueryResolverConfig.Operator.GT)) {
                criteria = criteria.and(key).gt(firstOne.getValue());
            } else if (op.equals(QueryResolverConfig.Operator.LT)) {
                criteria = criteria.and(key).lt(firstOne.getValue());
            } else if (op.equals(QueryResolverConfig.Operator.GTE)) {
                criteria = criteria.and(key).gte(firstOne.getValue());
            } else if (op.equals(QueryResolverConfig.Operator.LTE)) {
                criteria = criteria.and(key).lte(firstOne.getValue());
            } else if (op.equals(QueryResolverConfig.Operator.NE)) {
                criteria = criteria.and(key).ne(firstOne.getValue());
            } else if (op.equals(QueryResolverConfig.Operator.REGEX)) {
                criteria = criteria.and(key).regex(String.valueOf(firstOne.getValue()), QueryResolverConfig.REGEX_OPTIONS);
            } else if (op.equals(QueryResolverConfig.Operator.IN)) {
                criteria = criteria.and(key).in((List<?>) firstOne.getValue());
            } else {
                throw new InvalidOperatorError(op);
            }
            CriteriaPart next = queue.poll();
            while (!isNull(next)) {
                op = next.getOperator();
                // TODO: table of processor: HashMap<String, Function> -> Criteria
                if (op.equals(QueryResolverConfig.Operator.EQ)) {
                    criteria = criteria.is(next.getValue());
                } else if (op.equals(QueryResolverConfig.Operator.GT)) {
                    criteria = criteria.gt(next.getValue());
                } else if (op.equals(QueryResolverConfig.Operator.LT)) {
                    criteria = criteria.lt(next.getValue());
                } else if (op.equals(QueryResolverConfig.Operator.GTE)) {
                    criteria = criteria.gte(next.getValue());
                } else if (op.equals(QueryResolverConfig.Operator.LTE)) {
                    criteria = criteria.lte(next.getValue());
                } else if (op.equals(QueryResolverConfig.Operator.NE)) {
                    criteria = criteria.ne(next.getValue());
                } else if (op.equals(QueryResolverConfig.Operator.REGEX)) {
                    criteria = criteria.regex(String.valueOf(next.getValue()), QueryResolverConfig.REGEX_OPTIONS);
                } else if (op.equals(QueryResolverConfig.Operator.IN)) {
                    criteria = criteria.in((List<?>) next.getValue());
                } else {
                    throw new InvalidOperatorError(op);
                }
                next = queue.poll();
            }
        }
        return criteria;
    }

    public static Sort buildSort(Collection<SortPart> parts) throws InvalidOrderError {
        Sort sort = Sort.unsorted();
        for (SortPart part : parts) {
            String key = part.getKey();
            String order = part.getOrder();
            if (order.equals(QueryResolverConfig.Operator.ASC)) {
                sort = sort.and(Sort.by(Sort.Direction.ASC, key));
            } else if (order.equals(QueryResolverConfig.Operator.DESC)) {
                sort = sort.and(Sort.by(Sort.Direction.DESC, key));
            } else {
                throw new InvalidOrderError(order);
            }
        }
        return sort;
    }

    public static Pageable buildPageable(int page, int size) {
        return size <= 0 ? Pageable.unpaged() : PageRequest.of(page, size);
    }
}
