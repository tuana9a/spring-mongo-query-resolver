package com.tuana9a.spring.mongo.qrisolver;

import com.tuana9a.spring.mongo.qrisolver.configs.QueryResolverConfig;
import com.tuana9a.spring.mongo.qrisolver.errors.Error;
import com.tuana9a.spring.mongo.qrisolver.errors.InvalidPartError;
import com.tuana9a.spring.mongo.qrisolver.payloads.CriteriaPart;
import com.tuana9a.spring.mongo.qrisolver.payloads.SortPart;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ToolBoxTests {
    @Test
    public void testIs() throws Error {
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("name==tuana9a"));
        Criteria criteria = ToolBox.buildCriteria(parts);
        Criteria desiredCriteria = Criteria.where("name").is("tuana9a");
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testNe() throws Error {
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("name!=tuana9a"));
        Criteria criteria = ToolBox.buildCriteria(parts);
        Criteria desiredCriteria = Criteria.where("name").ne("tuana9a");
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testGt() throws Error {
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("age>10"));
        Criteria criteria = ToolBox.buildCriteria(parts);
        Criteria desiredCriteria = Criteria.where("age").gt(10);
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testLt() throws Error {
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("age<10"));
        Criteria criteria = ToolBox.buildCriteria(parts);
        Criteria desiredCriteria = Criteria.where("age").lt(10);
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testGte() throws Error {
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("age>=10"));
        Criteria criteria = ToolBox.buildCriteria(parts);
        Criteria desiredCriteria = Criteria.where("age").gte(10);
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testLte() throws Error {
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("age<=10"));
        Criteria criteria = ToolBox.buildCriteria(parts);
        Criteria desiredCriteria = Criteria.where("age").lte(10);
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testIn() throws Error {
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("ids@=1;2;3"));
        Criteria criteria = ToolBox.buildCriteria(parts);
        Criteria desiredCriteria = Criteria.where("ids").in(1, 2, 3);
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testInDelimiter() throws Error {
        QueryResolverConfig.LIST_VALUE_DELIMITER = ":"; // FIXME: not sure if it's affect other test
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("ids@=1:2:3"));
        Criteria criteria = ToolBox.buildCriteria(parts);
        Criteria desiredCriteria = Criteria.where("ids").in(1, 2, 3);
        Assertions.assertEquals(criteria, desiredCriteria);
        QueryResolverConfig.LIST_VALUE_DELIMITER = ";";
    }

    @Test
    public void testEqNull() throws Error {
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("name==null"));
        Criteria criteria = ToolBox.buildCriteria(parts);
        Assertions.assertEquals(criteria, Criteria.where("name").is(null));
    }

    @Test
    public void testEqUndefined() throws Error {
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("name==undefined"));
        Criteria criteria = ToolBox.buildCriteria(parts);
        Assertions.assertEquals(criteria, Criteria.where("name").is(null));
    }

    @Test
    public void testLtGt() throws Error {
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("age>5"));
        parts.add(ToolBox.buildCriteriaPart("age<10"));
        parts.add(ToolBox.buildCriteriaPart("age!=8"));
        Criteria criteria = ToolBox.buildCriteria(parts);
        Assertions.assertEquals(criteria, Criteria.where("age").gt(5).lt(10).ne(8));
    }

    @Test
    public void testCriteria() throws Error {
        String q = "age>5,age<10,age!=8,name==tuana9a,graduate@=prim;high;uni,year>=1991,year<=2003";
        List<CriteriaPart> parts = new LinkedList<>();
        for (String x : q.split(",")) {
            parts.add(ToolBox.buildCriteriaPart(x));
        }
        Criteria criteria = ToolBox.buildCriteria(parts);
        Criteria desiredCriteria = Criteria.where("age").gt(5).lt(10).ne(8)
                .and("name").is("tuana9a")
                .and("graduate").in("prim", "high", "uni")
                .and("year").gte(1991).lte(2003);
        Assertions.assertEquals(criteria.getCriteriaObject(), desiredCriteria.getCriteriaObject());
    }

    @Test
    public void testInvalidPart() {
        String q = "age>5,age<10,age!=8,name==tuana9a,error-should-be-thrown";
        Assertions.assertThrows(InvalidPartError.class, () -> {
            List<CriteriaPart> parts = new LinkedList<>();
            for (String x : q.split(",")) {
                parts.add(ToolBox.buildCriteriaPart(x));
            }
        });
    }

    @Test
    public void testSort() throws Error {
        String s = "age=-1,address=1";
        Collection<SortPart> parts = new LinkedList<>();
        for (String x : s.split(",")) {
            parts.add(ToolBox.buildSortPart(x));
        }
        Sort sort = ToolBox.buildSort(parts);
        /* this will not work
        Sort desiredSort = Sort.by(Sort.Direction.ASC, "address").and(Sort.by(Sort.Direction.DESC, "address"));
        */
        Sort desiredSort = Sort.by(Sort.Direction.DESC, "age").and(Sort.by(Sort.Direction.ASC, "address"));
        Assertions.assertEquals(sort, desiredSort);
    }

    @Test
    public void testCriteriaRegex() throws Error {
        QueryResolverConfig.REGEX_OPTIONS = "i";
        String q = "name*=tuana9a,address*=^9a$";
        List<CriteriaPart> parts = new LinkedList<>();
        for (String x : q.split(",")) {
            parts.add(ToolBox.buildCriteriaPart(x));
        }
        Criteria criteria = ToolBox.buildCriteria(parts);
        Criteria desiredCriteria = Criteria.where("address").regex("^9a$", "i").and("name").regex("tuana9a", "i");
        Assertions.assertEquals(criteria, desiredCriteria);
        QueryResolverConfig.REGEX_OPTIONS = "";
    }

    @Test
    public void testPagable() {
        Pageable pageable = ToolBox.buildPageable(0, 10);
        Pageable desiredPageable = PageRequest.of(0, 10);
        Assertions.assertEquals(pageable, desiredPageable);
    }

    @Test
    public void testPagable2() {
        Pageable pageable = ToolBox.buildPageable(1234, 0);
        Pageable desiredPageable = Pageable.unpaged();
        Assertions.assertEquals(pageable, desiredPageable);
    }

    @Test
    public void testPagable3() {
        Pageable pageable = ToolBox.buildPageable(1234, -10);
        Pageable desiredPageable = Pageable.unpaged();
        Assertions.assertEquals(pageable, desiredPageable);
    }
}
