package com.techproai.merij.utils;

import com.techproai.merij.builders.CriteriaQueryBuilder;
import com.techproai.merij.builders.SortQueryBuilder;
import com.techproai.merij.exceptions.CriteriaOperationNotSupported;
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
    private List<SortQueryBuilder> sortBuilders;
    private List<CriteriaQueryBuilder> queryBuilders;

    public QueryExecutor(Class<T> klass, MongoTemplate mongoTemplate) {
        this.queryBuilders = new LinkedList<>();
        this.sortBuilders = new LinkedList<>();
        this.page = 0;
        this.size = 0;
        this.klass = klass;
        this.mongoTemplate = mongoTemplate;
    }

    public QueryExecutor<T> queries(List<CriteriaQueryBuilder> queries) {
        this.queryBuilders = queries;
        return this;
    }

    public QueryExecutor<T> sorts(List<SortQueryBuilder> sorts) {
        this.sortBuilders = sorts;
        return this;
    }

    public QueryExecutor<T> and(CriteriaQueryBuilder builder) {
        this.queryBuilders.add(builder);
        return this;
    }

    public QueryExecutor<T> and(String query) {
        this.queryBuilders.add(CriteriaQueryBuilder.resolve(query));
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

    public List<T> find() throws MerijException {
        Criteria criteria = new Criteria();
        List<CriteriaQueryBuilder> reducedQueryBuilders = CriteriaQueryBuilder.reduce(queryBuilders);

        // must reduce before build
        if (reducedQueryBuilders.size() > 0) {
            CriteriaQueryBuilder first = reducedQueryBuilders.get(0);
            criteria = first.first();
            for (int i = 1, criteriaQueryBuilderListSize = reducedQueryBuilders.size(); i < criteriaQueryBuilderListSize; i++) {
                CriteriaQueryBuilder builder = reducedQueryBuilders.get(i);
                builder.chain(criteria);
            }
        }

        Sort sort = Sort.unsorted();
        if (sortBuilders.size() > 0) {
            SortQueryBuilder first = sortBuilders.get(0);
            sort = first.first();
            for (int i = 1, criteriaQueryBuilderListSize = sortBuilders.size(); i < criteriaQueryBuilderListSize; i++) {
                SortQueryBuilder builder = sortBuilders.get(i);
                builder.chain(sort);
            }
        }

        Pageable pageable = (page == 0 && size == 0) ? Pageable.unpaged() : PageRequest.of(page, size);
        Query query = new Query(criteria);
        return mongoTemplate.find(query.with(pageable).with(sort), klass);
    }
}
