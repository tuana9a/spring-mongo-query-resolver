package com.techproai.merij;

import com.techproai.merij.exceptions.CriteriaOperationNotSupported;
import com.techproai.merij.exceptions.CriteriaQueryLogicException;
import com.techproai.merij.exceptions.MerijException;
import com.techproai.merij.exceptions.SortOperationNotSupported;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.LinkedList;
import java.util.List;

public class QueryExecutor<T> {
    private final Class<T> klass;
    private final MongoTemplate mongoTemplate;
    private int page;
    private int size;
    private List<SortRequest> sortRequests;
    private List<CriteriaRequest> criteriaRequests;

    public QueryExecutor(Class<T> klass, MongoTemplate mongoTemplate) {
        this.criteriaRequests = new LinkedList<>();
        this.sortRequests = new LinkedList<>();
        this.page = 0;
        this.size = 0;
        this.klass = klass;
        this.mongoTemplate = mongoTemplate;
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

    public QueryExecutor<T> and(String query) {
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
        if (reducedCriteriaRequests.size() > 0) {
            CriteriaRequest first = reducedCriteriaRequests.get(0);
            criteria = first.init();
            int length = reducedCriteriaRequests.size();
            for (int i = 1; i < length; i++) {
                CriteriaRequest criteriaRequest = reducedCriteriaRequests.get(i);
                criteriaRequest.chain(criteria);
            }
        }
        return criteria;
    }

    public Sort sort() throws SortOperationNotSupported {
        Sort sort = Sort.unsorted();
        if (sortRequests.size() > 0) {
            SortRequest first = sortRequests.get(0);
            sort = first.first();
            int length = sortRequests.size();
            for (int i = 1; i < length; i++) {
                SortRequest sortRequest = sortRequests.get(i);
                sortRequest.chain(sort);
            }
        }
        return sort;
    }

    public List<T> find() throws MerijException {
        Criteria criteria = this.criteria();
        Sort sort = this.sort();
        Pageable pageable = (page == 0 && size == 0) ? Pageable.unpaged() : PageRequest.of(page, size);
        Query query = new Query(criteria);
        return mongoTemplate.find(query.with(pageable).with(sort), klass);
    }
}
