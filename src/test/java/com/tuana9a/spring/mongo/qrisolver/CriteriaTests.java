package com.tuana9a.spring.mongo.qrisolver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Criteria;

public class CriteriaTests {
    @Test
    public void canWeChainLikeThis() {
        Criteria criteria = Criteria.where("name").is("tuana9a");
        criteria.and("age").is(23);
        criteria.and("single").is(true);
        Assertions.assertEquals(criteria, Criteria
                .where("name").is("tuana9a")
                .and("age").is(23)
                .and("single").is(true));
    }

    @Test
    public void oopsWeCanNotChainLikeThis() {
        Criteria criteria = Criteria.where("name").is("tuana9a");
        criteria.and("age").gt(18);
        criteria.and("age").lt(23); // same key must be continuous chaining
        criteria.and("single").is(true);
        Assertions.assertNotEquals(criteria, Criteria
                .where("name").is("tuana9a")
                .and("age").gt(18).lt(23)
                .and("single").is(true));
    }

    @Test
    public void thisNotWorkAsWell() {
        Criteria criteria = Criteria.where("name").is("tuana9a");
        criteria.andOperator(Criteria.where("age").gt(18).lt(23));
        criteria.andOperator(Criteria.where("single").is(true));
        Assertions.assertNotEquals(criteria, Criteria
                .where("name").is("tuana9a")
                .and("age").gt(18).lt(23)
                .and("single").is(true));
    }
}
