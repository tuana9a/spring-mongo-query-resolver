package com.tuana9a.spring.mongo.qrisolver;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

public class ToolBoxTests {
    @Test
    public void testIs() throws Error {
        Opts opts = new Opts();
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("name==tuana9a", opts));
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Criteria desiredCriteria = Criteria.where("name").is("tuana9a");
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testNe() throws Error {
        Opts opts = new Opts();
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("name!=tuana9a", opts));
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Criteria desiredCriteria = Criteria.where("name").ne("tuana9a");
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testGt() throws Error {
        Opts opts = new Opts();
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("age>10", opts));
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Criteria desiredCriteria = Criteria.where("age").gt(10);
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testLt() throws Error {
        Opts opts = new Opts();
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("age<10", opts));
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Criteria desiredCriteria = Criteria.where("age").lt(10);
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testGte() throws Error {
        Opts opts = new Opts();
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("age>=10", opts));
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Criteria desiredCriteria = Criteria.where("age").gte(10);
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testLte() throws Error {
        Opts opts = new Opts();
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("age<=10", opts));
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Criteria desiredCriteria = Criteria.where("age").lte(10);
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testIn() throws Error {
        Opts opts = new Opts();
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("ids@=1;2;3", opts));
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Criteria desiredCriteria = Criteria.where("ids").in(1, 2, 3);
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testInDelimiter() throws Error {
        Opts opts = new Opts();
        opts.inOperatorDelimiter = ":";
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("ids@=1:2:3", opts));
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Criteria desiredCriteria = Criteria.where("ids").in(1, 2, 3);
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testEqNull() throws Error {
        Opts opts = new Opts();
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("name==null", opts));
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Assertions.assertEquals(criteria, Criteria.where("name").is(null));
    }

    @Test
    public void testEqUndefined() throws Error {
        Opts opts = new Opts();
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("name==undefined", opts));
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Assertions.assertEquals(criteria, Criteria.where("name").is(null));
    }

    @Test
    public void testLtGt() throws Error {
        Opts opts = new Opts();
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("age>5", opts));
        parts.add(ToolBox.buildCriteriaPart("age<10", opts));
        parts.add(ToolBox.buildCriteriaPart("age!=8", opts));
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Assertions.assertEquals(criteria, Criteria.where("age").gt(5).lt(10).ne(8));
    }

    @Test
    public void testCriteria() throws Error {
        String q = "age>5,age<10,age!=8,name==tuana9a,graduate@=prim;high;uni,year>=1991,year<=2003";
        Opts opts = new Opts();
        List<CriteriaPart> parts = new LinkedList<>();
        for (String x : q.split(",")) {
            parts.add(ToolBox.buildCriteriaPart(x, opts));
        }
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Criteria desiredCriteria = Criteria.where("age").gt(5).lt(10).ne(8)
                .and("name").is("tuana9a")
                .and("graduate").in("prim", "high", "uni")
                .and("year").gte(1991).lte(2003);
        Assertions.assertEquals(criteria.getCriteriaObject(), desiredCriteria.getCriteriaObject());
    }

    @Test
    public void testInvalidPart() {
        String q = "age>5,age<10,age!=8,name==tuana9a,error-should-be-thrown";
        Opts opts = new Opts();
        Assertions.assertThrows(InvalidPartError.class, () -> {
            List<CriteriaPart> parts = new LinkedList<>();
            for (String x : q.split(",")) {
                parts.add(ToolBox.buildCriteriaPart(x, opts));
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
        Opts opts = new Opts();
        opts.regexOptions = "i";
        String q = "name*=tuana9a,address*=^9a$";
        List<CriteriaPart> parts = new LinkedList<>();
        for (String x : q.split(",")) {
            parts.add(ToolBox.buildCriteriaPart(x, opts));
        }
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Criteria desiredCriteria = Criteria.where("address").regex("^9a$", "i").and("name").regex("tuana9a", "i");
        Assertions.assertEquals(criteria, desiredCriteria);
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
