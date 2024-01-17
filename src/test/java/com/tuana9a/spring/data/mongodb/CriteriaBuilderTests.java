package com.tuana9a.spring.data.mongodb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Criteria;

public class CriteriaBuilderTests {
    @Test
    public void testIs() {
        Criteria criteria = CriteriaBuilder.from("name==tuana9a").toCriteria();
        Assertions.assertEquals(criteria, Criteria.where("name").is("tuana9a"));
    }

    @Test
    public void testNe() {
        Criteria criteria = CriteriaBuilder.from("name!=tuana9a").toCriteria();
        Assertions.assertEquals(criteria, Criteria.where("name").ne("tuana9a"));
    }

    @Test
    public void testGt() {
        Criteria criteria = CriteriaBuilder.from("age>10").toCriteria();
        Assertions.assertEquals(criteria, Criteria.where("age").gt(10));
    }

    @Test
    public void testLt() {
        Criteria criteria = CriteriaBuilder.from("age<10").toCriteria();
        Assertions.assertEquals(criteria, Criteria.where("age").lt(10));
    }

    @Test
    public void testGte() {
        Criteria criteria = CriteriaBuilder.from("age>=10").toCriteria();
        Assertions.assertEquals(criteria, Criteria.where("age").gte(10));
    }

    @Test
    public void testLte() {
        Criteria criteria = CriteriaBuilder.from("age<=10").toCriteria();
        Assertions.assertEquals(criteria, Criteria.where("age").lte(10));
    }

    @Test
    public void testIn() {
        Criteria criteria = CriteriaBuilder.from("ids@=1;2;3").toCriteria();
        Assertions.assertEquals(criteria, Criteria.where("ids").in(1, 2, 3));
    }

    @Test
    public void testEqNull() {
        Criteria criteria = CriteriaBuilder.from("name==null").toCriteria();
        Assertions.assertEquals(criteria, Criteria.where("name").is(null));
    }

    @Test
    public void testEqUndefined() {
        Criteria criteria = CriteriaBuilder.from("name==undefined").toCriteria();
        Assertions.assertEquals(criteria, Criteria.where("name").is(null));
    }

    @Test
    public void testLtGt() {
        CriteriaBuilder c1 = CriteriaBuilder.from("age>5");
        CriteriaBuilder c2 = CriteriaBuilder.from("age<10");
        CriteriaBuilder c3 = CriteriaBuilder.from("age!=8");
        Criteria criteria = c1.chain(c2).chain(c3).toCriteria();
        Assertions.assertEquals(criteria, Criteria.where("age").gt(5).lt(10).ne(8));
    }
}
