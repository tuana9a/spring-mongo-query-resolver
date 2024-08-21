package com.tuana9a.spring.mongo.qrisolver;

import com.tuana9a.spring.mongo.qrisolver.configs.Config;
import com.tuana9a.spring.mongo.qrisolver.errors.Error;
import com.tuana9a.spring.mongo.qrisolver.errors.InvalidPartError;
import com.tuana9a.spring.mongo.qrisolver.resolvers.CriteriaResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Criteria;

public class CriteriaResolverTests {
    @Test
    public void testBasicUsage() throws Error {
        String q = "age>5,age<10,age!=8,name==tuana9a,graduate@=prim;high;uni,year>=1991,year<=2003,deleted==false";
        Criteria criteria = CriteriaResolver.resolve(q);
        Criteria desiredCriteria = Criteria.where("age").gt(5).lt(10).ne(8)
                .and("name").is("tuana9a")
                .and("graduate").in("prim", "high", "uni")
                .and("year").gte(1991).lte(2003)
                .and("deleted").is(false);
        Assertions.assertEquals(criteria.getCriteriaObject(), desiredCriteria.getCriteriaObject());
    }

    @Test
    public void testInvalidPart() {
        String q = "age>5,age<10,age!=8,name==tuana9a,error-should-be-thrown";
        Assertions.assertThrows(InvalidPartError.class, () -> CriteriaResolver.resolve(q));
    }

    @Test
    public void testGtLtNe() throws Error {
        String q = "age>5,age<10,age!=8";
        Criteria criteria = CriteriaResolver.resolve(q);
        Assertions.assertEquals(criteria, Criteria.where("age").gt(5).lt(10).ne(8));
    }

    @Test
    public void testIs() throws Error {
        Criteria criteria = CriteriaResolver.resolve("name==tuana9a");
        Criteria desiredCriteria = Criteria.where("name").is("tuana9a");
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testNe() throws Error {
        Criteria criteria = CriteriaResolver.resolve("name!=tuana9a");
        Criteria desiredCriteria = Criteria.where("name").ne("tuana9a");
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testGt() throws Error {
        Criteria criteria = CriteriaResolver.resolve("age>10");
        Criteria desiredCriteria = Criteria.where("age").gt(10);
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testLt() throws Error {
        Criteria criteria = CriteriaResolver.resolve("age<10");
        Criteria desiredCriteria = Criteria.where("age").lt(10);
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testGte() throws Error {
        Criteria criteria = CriteriaResolver.resolve("age>=10");
        Criteria desiredCriteria = Criteria.where("age").gte(10);
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testLte() throws Error {
        Criteria criteria = CriteriaResolver.resolve("age<=10");
        Criteria desiredCriteria = Criteria.where("age").lte(10);
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testIn() throws Error {
        Criteria criteria = CriteriaResolver.resolve("ids@=1;2;3");
        Criteria desiredCriteria = Criteria.where("ids").in(1, 2, 3);
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testInDelimiter() throws Error {
        Config.LIST_VALUE_DELIMITER = ":"; // WARN: changing static variable affect all other tests
        Criteria criteria = CriteriaResolver.resolve("ids@=1:2:3");
        Criteria desiredCriteria = Criteria.where("ids").in(1, 2, 3);
        Assertions.assertEquals(criteria, desiredCriteria);
        Config.LIST_VALUE_DELIMITER = ";"; // reset it back to default
    }

    @Test
    public void testEqNull() throws Error {
        Criteria criteria = CriteriaResolver.resolve("name==null");
        Assertions.assertEquals(criteria, Criteria.where("name").is(null));
    }

    @Test
    public void testEqUndefined() throws Error {
        Criteria criteria = CriteriaResolver.resolve("name==undefined");
        Assertions.assertEquals(criteria, Criteria.where("name").is(null));
    }

    @Test
    public void testRegex() throws Error {
        Config.REGEX_OPTIONS = "i"; // WARN: changing static variable affect all other tests
        Criteria criteria = CriteriaResolver.resolve("name*=tuana9a,address*=^9a$");
        Criteria desiredCriteria = Criteria.where("address").regex("^9a$", "i").and("name").regex("tuana9a", "i");
        Assertions.assertEquals(criteria, desiredCriteria);
        Config.REGEX_OPTIONS = ""; // reset it back to default
    }
}
