package com.tuana9a.spring.data.mongodb;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

public class QueryResolver {
    protected final Opts opts;

    public QueryResolver() {
        this(Opts.DEFAULT);
    }

    public QueryResolver(Opts opts) {
        this.opts = opts;
    }

    public Criteria resolveCriteria(String queries) {
        return resolveCriteria(Arrays.asList(queries.split(opts.delimiter)));
    }

    public Criteria resolveCriteria(Collection<String> queries) {
        Criteria criteria = new Criteria();
        Map<String, CriteriaBuilder> map = new HashMap<>();
        queries.stream()
                .map(x -> CriteriaBuilder.from(x, opts))
                .filter(CriteriaBuilder::isValid)
                .filter(x -> !opts.ignoredKeys.contains(x.key))
                .forEach(x -> {
                    String key = x.getKey();
                    CriteriaBuilder existed = map.get(key);
                    if (isNull(existed)) map.put(key, x);
                    else existed.chain(x);
                });
        map.values().forEach(x -> x.toCriteria(criteria));
        return criteria;
    }

    public Sort resolveSort(String sorts) {
        return resolveSort(Arrays.asList(sorts.split(opts.delimiter)));
    }

    public Sort resolveSort(Collection<String> sorts) {
        return sorts.stream()
                .map(SortBuilder::from)
                .filter(SortBuilder::isValid)
                .filter(x -> !opts.ignoredKeys.contains(x.key))
                .reduce(Sort.unsorted(), (sort, x) -> x.toSort(sort), Sort::and);
    }

    public Pageable resolvePaging(int page, int size) {
        return size == 0 ? Pageable.unpaged() : PageRequest.of(page, size);
    }

    public Query resolveQuery(String queries, String sorts, int page, int size) {
        Criteria criteria = this.resolveCriteria(queries);
        Sort sort = this.resolveSort(sorts);
        Pageable pageable = this.resolvePaging(page, size);
        return new Query(criteria).with(sort).with(pageable);
    }
}
