package com.tuana9a.merij;

import com.tuana9a.merij.exceptions.CriteriaOperationNotSupported;
import com.tuana9a.merij.exceptions.QueryPatternNotMatchException;
import com.tuana9a.merij.requests.CriteriaRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Criteria;

public class CriteriaRequestTests {
    @Test
    public void testIs() throws CriteriaOperationNotSupported, QueryPatternNotMatchException {
        Criteria criteria = CriteriaRequest.resolve("name==tuana9a").toCriteria();
        Assertions.assertEquals(criteria, Criteria.where("name").is("tuana9a"));
    }

    @Test
    public void testNe() throws CriteriaOperationNotSupported, QueryPatternNotMatchException {
        Criteria criteria = CriteriaRequest.resolve("name!=tuana9a").toCriteria();
        Assertions.assertEquals(criteria, Criteria.where("name").ne("tuana9a"));
    }

    @Test
    public void testGt() throws CriteriaOperationNotSupported, QueryPatternNotMatchException {
        Criteria criteria = CriteriaRequest.resolve("age>10").toCriteria();
        Assertions.assertEquals(criteria, Criteria.where("age").gt(10));
    }

    @Test
    public void testLt() throws CriteriaOperationNotSupported, QueryPatternNotMatchException {
        Criteria criteria = CriteriaRequest.resolve("age<10").toCriteria();
        Assertions.assertEquals(criteria, Criteria.where("age").lt(10));
    }

    @Test
    public void testGte() throws CriteriaOperationNotSupported, QueryPatternNotMatchException {
        Criteria criteria = CriteriaRequest.resolve("age>=10").toCriteria();
        Assertions.assertEquals(criteria, Criteria.where("age").gte(10));
    }

    @Test
    public void testLte() throws CriteriaOperationNotSupported, QueryPatternNotMatchException {
        Criteria criteria = CriteriaRequest.resolve("age<=10").toCriteria();
        Assertions.assertEquals(criteria, Criteria.where("age").lte(10));
    }

    @Test
    public void testIn() throws CriteriaOperationNotSupported, QueryPatternNotMatchException {
        Criteria criteria = CriteriaRequest.resolve("ids@=1;2;3").toCriteria();
        Assertions.assertEquals(criteria, Criteria.where("ids").in(1, 2, 3));
    }

    @Test
    public void testEqNull() throws CriteriaOperationNotSupported, QueryPatternNotMatchException {
        Criteria criteria = CriteriaRequest.resolve("name==null").toCriteria();
        Assertions.assertEquals(criteria, Criteria.where("name").is(null));
    }

    @Test
    public void testEqUndefined() throws CriteriaOperationNotSupported, QueryPatternNotMatchException {
        Criteria criteria = CriteriaRequest.resolve("name==undefined").toCriteria();
        Assertions.assertEquals(criteria, Criteria.where("name").is(null));
    }

    @Test
    public void testLtGt() throws CriteriaOperationNotSupported, QueryPatternNotMatchException {
        CriteriaRequest c1 = CriteriaRequest.resolve("age>5");
        CriteriaRequest c2 = CriteriaRequest.resolve("age<10");
        CriteriaRequest c3 = CriteriaRequest.resolve("age!=8");
        Criteria criteria = c1.toCriteria(c2.toCriteria(c3.toCriteria()));
        Assertions.assertEquals(criteria, Criteria.where("age").gt(5).lt(10).ne(8));
    }
}
