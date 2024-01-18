package com.tuana9a.spring.data.mongodb;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

public class ToolBoxTests {
    @Test
    public void testIs() {
        Opts opts = new Opts();
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("name==tuana9a", opts));
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Criteria desiredCriteria = Criteria.where("name").is("tuana9a");
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testNe() {
        Opts opts = new Opts();
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("name!=tuana9a", opts));
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Criteria desiredCriteria = Criteria.where("name").ne("tuana9a");
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testGt() {
        Opts opts = new Opts();
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("age>10", opts));
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Criteria desiredCriteria = Criteria.where("age").gt(10);
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testLt() {
        Opts opts = new Opts();
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("age<10", opts));
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Criteria desiredCriteria = Criteria.where("age").lt(10);
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testGte() {
        Opts opts = new Opts();
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("age>=10", opts));
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Criteria desiredCriteria = Criteria.where("age").gte(10);
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testLte() {
        Opts opts = new Opts();
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("age<=10", opts));
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Criteria desiredCriteria = Criteria.where("age").lte(10);
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testIn() {
        Opts opts = new Opts();
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("ids@=1;2;3", opts));
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Criteria desiredCriteria = Criteria.where("ids").in(1, 2, 3);
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testInDelimiter() {
        Opts opts = new Opts();
        opts.inOperatorDelimiter = ":";
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("ids@=1:2:3", opts));
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Criteria desiredCriteria = Criteria.where("ids").in(1, 2, 3);
        Assertions.assertEquals(criteria, desiredCriteria);
    }

    @Test
    public void testEqNull() {
        Opts opts = new Opts();
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("name==null", opts));
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Assertions.assertEquals(criteria, Criteria.where("name").is(null));
    }

    @Test
    public void testEqUndefined() {
        Opts opts = new Opts();
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("name==undefined", opts));
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Assertions.assertEquals(criteria, Criteria.where("name").is(null));
    }

    @Test
    public void testLtGt() {
        Opts opts = new Opts();
        List<CriteriaPart> parts = new LinkedList<CriteriaPart>();
        parts.add(ToolBox.buildCriteriaPart("age>5", opts));
        parts.add(ToolBox.buildCriteriaPart("age<10", opts));
        parts.add(ToolBox.buildCriteriaPart("age!=8", opts));
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Assertions.assertEquals(criteria, Criteria.where("age").gt(5).lt(10).ne(8));
    }

    @Test
    public void testCriteria() {
        String q = "age>5,age<10,age!=8,name==tuana9a,graduate@=prim;high;uni,year>=1991,year<=2003,error-should-be-ignored";
        Opts opts = new Opts();
        Collection<CriteriaPart> parts = Arrays.stream(q.split(",")).map(x -> ToolBox.buildCriteriaPart(x, opts)).filter(x -> !x.isError).collect(Collectors.toList());
        Criteria criteria = ToolBox.buildCriteria(parts, opts);
        Criteria desiredCriteria = Criteria.where("age").gt(5).lt(10).ne(8)
                .and("name").is("tuana9a")
                .and("graduate").in("prim", "high", "uni")
                .and("year").gte(1991).lte(2003);
        Assertions.assertEquals(criteria.getCriteriaObject(), desiredCriteria.getCriteriaObject());
    }

    @Test
    public void testSort() {
        String s = "age=-1,address=1";
        Collection<SortPart> parts = Arrays.stream(s.split(",")).map(x -> ToolBox.buildSortPart(x)).collect(Collectors.toList());
        Sort sort = ToolBox.buildSort(parts);
        // Sort desiredSort = Sort.by(Sort.Direction.ASC, "address").and(Sort.by(Sort.Direction.DESC, "address")); // this will not work, below will work
        Sort desiredSort = Sort.by(Sort.Direction.DESC, "age").and(Sort.by(Sort.Direction.ASC, "address"));
        Assertions.assertEquals(sort, desiredSort);
    }

    @Test
    public void testCriteriaRegex() {
        Opts opts = new Opts();
        opts.regexOptions = "i";
        String q = "name*=tuana9a,address*=^9a$";
        List<CriteriaPart> parts = Arrays.asList(q.split(",")).stream().map(x -> ToolBox.buildCriteriaPart(x, opts)).collect(Collectors.toList());
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
