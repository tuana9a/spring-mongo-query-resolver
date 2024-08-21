package com.tuana9a.spring.mongo.qrisolver;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import lombok.extern.slf4j.Slf4j;
import static java.util.Objects.isNull;
import java.util.Arrays;

public class ToolBox {
    public static CriteriaPart buildCriteriaPart(String input, Opts opts) throws Error {
        Pattern pattern = Pattern.compile(CriteriaPart.REGEX_PATTERN);
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find()) {
            throw new InvalidPartError(input, "not match " + CriteriaPart.REGEX_PATTERN);
        }
        String key = matcher.group(1).trim();
        String op = matcher.group(2).trim();
        String stringValue = matcher.group(3);
        if (op.equals(Operator.IN)) {
            List<?> value = Arrays.stream(stringValue.split(opts.inOperatorDelimiter))
                    .map(x -> Utils.resolve(x.trim()))
                    .collect(Collectors.toList());
            return new CriteriaPart(key, op, value);
        }
        Object value = Utils.resolve(stringValue.trim());
        return new CriteriaPart(key, op, value);
    }

    public static SortPart buildSortPart(String input) throws Error {
        Pattern pattern = Pattern.compile(SortPart.REGEX_PATTERN);
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find()) {
            throw new InvalidPartError(input, "not match " + SortPart.REGEX_PATTERN);
        }
        String key = matcher.group(1).trim();
        String order = matcher.group(2).trim();
        return new SortPart(key, order);
    }

    public static Criteria buildCriteria(Collection<CriteriaPart> parts) throws Error {
        return buildCriteria(parts, Opts.DEFAULT);
    }

    public static Criteria buildCriteria(Collection<CriteriaPart> parts, Opts opts)  throws Error{
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
            if (op.equals(Operator.EQ)) {
                criteria = criteria.and(key).is(firstOne.getValue());
            } else if (op.equals(Operator.GT)) {
                criteria = criteria.and(key).gt(firstOne.getValue());
            } else if (op.equals(Operator.LT)) {
                criteria = criteria.and(key).lt(firstOne.getValue());
            } else if (op.equals(Operator.GTE)) {
                criteria = criteria.and(key).gte(firstOne.getValue());
            } else if (op.equals(Operator.LTE)) {
                criteria = criteria.and(key).lte(firstOne.getValue());
            } else if (op.equals(Operator.NE)) {
                criteria = criteria.and(key).ne(firstOne.getValue());
            } else if (op.equals(Operator.REGEX)) {
                criteria = criteria.and(key).regex(String.valueOf(firstOne.getValue()), opts.regexOptions);
            } else if (op.equals(Operator.IN)) {
                criteria = criteria.and(key).in((List<?>) firstOne.getValue());
            } else {
                throw new InvalidOperatorError(op);
            }
            CriteriaPart next = queue.poll();
            while (!isNull(next)) {
                op = next.getOperator();
                // TODO: table of processor: HashMap<String, Function> -> Criteria
                if (op.equals(Operator.EQ)) {
                    criteria = criteria.is(next.getValue());
                } else if (op.equals(Operator.GT)) {
                    criteria = criteria.gt(next.getValue());
                } else if (op.equals(Operator.LT)) {
                    criteria = criteria.lt(next.getValue());
                } else if (op.equals(Operator.GTE)) {
                    criteria = criteria.gte(next.getValue());
                } else if (op.equals(Operator.LTE)) {
                    criteria = criteria.lte(next.getValue());
                } else if (op.equals(Operator.NE)) {
                    criteria = criteria.ne(next.getValue());
                } else if (op.equals(Operator.REGEX)) {
                    criteria = criteria.regex(String.valueOf(next.getValue()), opts.regexOptions);
                } else if (op.equals(Operator.IN)) {
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
            if (order.equals(Operator.ASC)) {
                sort = sort.and(Sort.by(Sort.Direction.ASC, key));
            } else if (order.equals(Operator.DESC)) {
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
