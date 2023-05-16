package com.tuana9a.merij;

import com.mongodb.client.result.DeleteResult;
import com.tuana9a.merij.exceptions.*;
import com.tuana9a.merij.requests.CriteriaRequest;
import com.tuana9a.merij.requests.SortRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;
import java.util.stream.Collectors;

public class QueryExecutor<T> {
    private Class<T> klass;
    private MongoTemplate mongoTemplate;
    private int page = 0;
    private int size = 0;
    private Collection<String> sorts = new LinkedList<>();
    private Collection<SortRequest> sortRequests = new LinkedList<>();
    private Collection<CriteriaRequest> criteriaRequests = new LinkedList<>();
    private final Collection<CriteriaRequest> andCriteriaRequests = new LinkedList<>();
    private Collection<String> queries = new LinkedList<>();
    private Set<String> dropKeys = new HashSet<>();

    public QueryExecutor() {
    }

    public QueryExecutor<T> klass(Class<T> klass) {
        this.klass = klass;
        return this;
    }

    public QueryExecutor<T> mongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        return this;
    }

    public QueryExecutor<T> criteriaRequests(List<CriteriaRequest> criteriaRequests) {
        this.criteriaRequests = criteriaRequests;
        return this;
    }

    public QueryExecutor<T> queries(Collection<String> queries) {
        this.queries = queries;
        return this;
    }

    public QueryExecutor<T> queries(String[] queries) {
        this.queries = Arrays.asList(queries);
        return this;
    }

    public QueryExecutor<T> dropKey(String... keys) {
        this.dropKeys = new HashSet<>(Arrays.asList(keys));
        return this;
    }

    public QueryExecutor<T> sortsRequests(List<SortRequest> sortRequests) {
        this.sortRequests = sortRequests;
        return this;
    }

    public QueryExecutor<T> sorts(Collection<String> sorts) {
        this.sorts = sorts;
        return this;
    }

    public QueryExecutor<T> sorts(String[] sorts) {
        this.sorts = Arrays.asList(sorts);
        return this;
    }

    public QueryExecutor<T> and(CriteriaRequest criteriaRequest) {
        this.andCriteriaRequests.add(criteriaRequest);
        return this;
    }

    public QueryExecutor<T> and(String query) {
        this.queries.add(query);
        return this;
    }

    public QueryExecutor<T> page(int page) {
        this.page = page;
        return this;
    }

    public QueryExecutor<T> size(int size) {
        this.size = size;
        return this;
    }

    public Criteria criteria() throws CriteriaOperationNotSupported, QueryPatternNotMatchException {
        Criteria criteria = new Criteria();
        for (String query : queries) {
            this.criteriaRequests.add(CriteriaRequest.resolve(query));
        }
        this.criteriaRequests.addAll(this.andCriteriaRequests);
        List<CriteriaRequest> filtered = criteriaRequests.stream()
                .filter(x -> !dropKeys.contains(x.key()))
                .collect(Collectors.toList());
        List<CriteriaRequest> reduced = CriteriaRequest.reduce(filtered);
        for (CriteriaRequest criteriaRequest : reduced) {
            criteria = criteriaRequest.chain(criteria);
        }
        return criteria;
    }

    public Sort sort() throws SortOperationNotSupported, SortPatternNotMatchException {
        Sort sort = Sort.unsorted();
        for (String s : sorts) {
            this.sortRequests.add(SortRequest.resolve(s));
        }
        List<SortRequest> filtered = sortRequests.stream()
                .filter(x -> !dropKeys.contains(x.key()))
                .collect(Collectors.toList());
        for (SortRequest sortRequest : filtered) {
            sort = sortRequest.and(sort);
        }
        return sort;
    }

    public List<T> find() throws MerijException {
        Criteria criteria = this.criteria();
        Sort sort = this.sort();
        Pageable pageable = size == 0 ? Pageable.unpaged() : PageRequest.of(page, size);
        Query query = new Query(criteria);
        return mongoTemplate.find(query.with(pageable).with(sort), klass);
    }

    public long delete() throws MerijException {
        Criteria criteria = this.criteria();
        Query query = new Query(criteria);
        DeleteResult result = mongoTemplate.remove(query, klass);
        return result.getDeletedCount();
    }
}
