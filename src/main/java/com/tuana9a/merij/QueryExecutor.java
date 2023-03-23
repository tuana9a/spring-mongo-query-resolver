package com.tuana9a.merij;

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
    private int page;
    private int size;
    private List<SortRequest> sortRequests;
    private List<CriteriaRequest> criteriaRequests;

    public QueryExecutor() {
        this.criteriaRequests = new LinkedList<>();
        this.sortRequests = new LinkedList<>();
        this.page = 0;
        this.size = 0;
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

    public QueryExecutor<T> queries(Collection<String> queries) throws QueryPatternNotMatchException {
        this.criteriaRequests = new LinkedList<>();
        for (String query : queries) {
            this.criteriaRequests.add(CriteriaRequest.resolve(query));
        }
        return this;
    }

    public QueryExecutor<T> queries(String[] queries) throws QueryPatternNotMatchException {
        this.criteriaRequests = new LinkedList<>();
        for (String query : queries) {
            this.criteriaRequests.add(CriteriaRequest.resolve(query));
        }
        return this;
    }

    public QueryExecutor<T> dropKey(String... keys) {
        Set<String> keySet = new HashSet<>(Arrays.asList(keys));
        this.criteriaRequests = this.criteriaRequests
                .stream()
                .filter(x -> !keySet.contains(x.key()))
                .collect(Collectors.toList());
        return this;
    }

    public QueryExecutor<T> sortsRequests(List<SortRequest> sortRequests) {
        this.sortRequests = sortRequests;
        return this;
    }

    public QueryExecutor<T> sorts(Collection<String> sorts) throws SortPatternNotMatchException {
        this.sortRequests = new LinkedList<>();
        for (String sort : sorts) {
            this.sortRequests.add(SortRequest.resolve(sort));
        }
        return this;
    }

    public QueryExecutor<T> sorts(String[] sorts) throws SortPatternNotMatchException {
        this.sortRequests = new LinkedList<>();
        for (String sort : sorts) {
            this.sortRequests.add(SortRequest.resolve(sort));
        }
        return this;
    }

    public QueryExecutor<T> and(CriteriaRequest criteriaRequest) {
        this.criteriaRequests.add(criteriaRequest);
        return this;
    }

    public QueryExecutor<T> and(String query) throws QueryPatternNotMatchException {
        this.criteriaRequests.add(CriteriaRequest.resolve(query));
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

    public Criteria criteria() throws CriteriaQueryLogicException, CriteriaOperationNotSupported {
        Criteria criteria = new Criteria();
        List<CriteriaRequest> reducedCriteriaRequests = CriteriaRequest.reduce(criteriaRequests);
        for (CriteriaRequest criteriaRequest : reducedCriteriaRequests) {
            criteria = criteriaRequest.and(criteria);
        }
        return criteria;
    }

    public Sort sort() throws SortOperationNotSupported {
        Sort sort = Sort.unsorted();
        for (SortRequest sortRequest : sortRequests) {
            sort = sortRequest.and(sort);
        }
        return sort;
    }

    public List<T> find() throws MerijException {
        Criteria criteria = this.criteria();
        Sort sort = this.sort();
        Pageable pageable = (page <= 0 && size <= 0) ? Pageable.unpaged() : PageRequest.of(page, size);
        Query query = new Query(criteria);
        return mongoTemplate.find(query.with(pageable).with(sort), klass);
    }
}
