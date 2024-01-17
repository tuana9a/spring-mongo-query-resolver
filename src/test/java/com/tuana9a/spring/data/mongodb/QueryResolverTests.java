package com.tuana9a.spring.data.mongodb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

public class QueryResolverTests {
    @Test
    public void testCriteriaBuilder() {
        Criteria criteria = new QueryResolver(new Opts().addIgnoredKeys("unknown"))
                .resolveCriteria("age>5,age<10,age!=8,name==tuana9a,graduate@=prim;high;uni,year>=1991,year<=2003,unknown==should-be-ignored");
        Criteria desiredCriteria = Criteria
                .where("age").gt(5).lt(10).ne(8)
                .and("name").is("tuana9a")
                .and("graduate").in("prim", "high", "uni")
                .and("year").gte(1991).lte(2003);
        Assertions.assertEquals(criteria.getCriteriaObject(), desiredCriteria.getCriteriaObject());
    }

    @Test
    public void testSortBuilder() {
        Sort sort = new QueryResolver(new Opts().addIgnoredKeys("haha", "year"))
                .resolveSort("age=-1,address==1,year=-1,haha=1");
        Sort desiredSort = Sort.by(Sort.Direction.DESC, "age")
                .and(Sort.by(Sort.Direction.ASC, "address"));
        Assertions.assertEquals(sort, desiredSort);
    }

    @Test
    public void testCriteriaBuilderWithSortBuilder() {
        Opts opts = new Opts();

        Criteria criteria = new QueryResolver(opts).resolveCriteria("age>5,age<10,age!=8");
        Criteria desiredCriteria = Criteria.where("age").gt(5).lt(10).ne(8);
        Assertions.assertEquals(criteria.getCriteriaObject(), desiredCriteria.getCriteriaObject());

        Sort sort = new QueryResolver(opts).resolveSort("age=-1,address==1");
        Sort desiredSort = Sort.by(Sort.Direction.DESC, "age").and(Sort.by(Sort.Direction.ASC, "address"));
        Assertions.assertEquals(sort, desiredSort);
    }

    @Test
    public void testCriteriaBuilderRegexOptions() {
        Opts opts = new Opts();
        opts.regexOptions = "i";
        Criteria criteria = new QueryResolver(opts).resolveCriteria("name*=tuana9a");
        Criteria desiredCriteria = Criteria.where("name").regex("tuana9a", "i");
        Assertions.assertEquals(criteria, desiredCriteria);
    }
}
