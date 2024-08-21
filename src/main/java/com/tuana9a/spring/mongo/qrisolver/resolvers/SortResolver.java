package com.tuana9a.spring.mongo.qrisolver.resolvers;

import com.tuana9a.spring.mongo.qrisolver.configs.Config;
import com.tuana9a.spring.mongo.qrisolver.configs.SupportedOperator;
import com.tuana9a.spring.mongo.qrisolver.errors.Error;
import com.tuana9a.spring.mongo.qrisolver.errors.InvalidOrderError;
import com.tuana9a.spring.mongo.qrisolver.payloads.SortPart;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;

public class SortResolver {
    public static Sort resolve(String input) throws Error {
        List<SortPart> parts = new LinkedList<>();
        for (String x : input.split(Config.DELIMITER)) {
            parts.add(SortPartResolver.resolve(x));
        }
        Sort sort = Sort.unsorted();
        for (SortPart part : parts) {
            String key = part.getKey();
            String order = part.getOrder();
            if (order.equals(SupportedOperator.ASC)) {
                sort = sort.and(Sort.by(Sort.Direction.ASC, key));
            } else if (order.equals(SupportedOperator.DESC)) {
                sort = sort.and(Sort.by(Sort.Direction.DESC, key));
            } else {
                throw new InvalidOrderError(order);
            }
        }
        return sort;
    }
}
