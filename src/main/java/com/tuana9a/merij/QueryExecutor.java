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

import java.util.LinkedList;
import java.util.List;

public class QueryExecutor<T> {
    private Class<T> klass;
    private MongoTemplate mongoTemplate;
    private int page;
    private int size;
    private List<SortRequest> sortRequests;
    private List<CriteriaRequest> criteriaRequests;

    /**
     * @deprecated use builder syntax instead
     */
    public QueryExecutor(Class<T> klass, MongoTemplate mongoTemplate) {
        this();
        this.klass = klass;
        this.mongoTemplate = mongoTemplate;
    }

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

    public QueryExecutor<T> queries(List<CriteriaRequest> queries) {
        this.criteriaRequests = queries;
        return this;
    }

    public QueryExecutor<T> sorts(List<SortRequest> sorts) {
        this.sortRequests = sorts;
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
        // must reduce before build
//        if (reducedCriteriaRequests.size() > 0) {
//            CriteriaRequest first = reducedCriteriaRequests.get(0);
//            criteria = first.init();
//            int length = reducedCriteriaRequests.size();
//            for (int i = 1; i < length; i++) {
//                CriteriaRequest criteriaRequest = reducedCriteriaRequests.get(i);
//                criteriaRequest.chain(criteria);
//            }
//        }
        for (CriteriaRequest criteriaRequest : reducedCriteriaRequests) {
            criteria = criteriaRequest.and(criteria);
        }
        return criteria;
    }

    public Sort sort() throws SortOperationNotSupported {
        Sort sort = Sort.unsorted();
        if (sortRequests.size() > 0) {
            SortRequest first = sortRequests.get(0);
            sort = first.toSort();
            int length = sortRequests.size();
            for (int i = 1; i < length; i++) {
                SortRequest sortRequest = sortRequests.get(i);
                sortRequest.and(sort);
            }
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
