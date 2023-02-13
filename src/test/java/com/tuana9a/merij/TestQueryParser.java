package com.tuana9a.merij;

import com.tuana9a.merij.exceptions.CriteriaOperationNotSupported;
import com.tuana9a.merij.exceptions.CriteriaQueryLogicException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TestQueryParser {
    @Test
    public void testIs() throws CriteriaQueryLogicException, CriteriaOperationNotSupported {
        List<String> queries = new LinkedList<>();
        queries.add("name==tuana9a");
        QueryExecutor<?> q = new QueryExecutor<>(null, null)
                .queries(queries.stream()
                        .map(x -> CriteriaRequest.resolve(x).withRegexOptions("i"))
                        .filter(CriteriaRequest::isValid)
                        .collect(Collectors.toList()));
        Criteria criteria = q.criteria();
        Assertions.assertEquals(criteria, Criteria.where("name").is("tuana9a"));
    }

    @Test
    public void testNe() throws CriteriaQueryLogicException, CriteriaOperationNotSupported {
        List<String> queries = new LinkedList<>();
        queries.add("name!=tuana9a");
        QueryExecutor<?> q = new QueryExecutor<>(null, null)
                .queries(queries.stream()
                        .map(x -> CriteriaRequest.resolve(x).withRegexOptions("i"))
                        .filter(CriteriaRequest::isValid)
                        .collect(Collectors.toList()));
        Criteria criteria = q.criteria();
        Assertions.assertEquals(criteria, Criteria.where("name").ne("tuana9a"));
    }

    @Test
    public void testGt() throws CriteriaQueryLogicException, CriteriaOperationNotSupported {
        List<String> queries = new LinkedList<>();
        queries.add("age>10");
        QueryExecutor<?> q = new QueryExecutor<>(null, null)
                .queries(queries.stream()
                        .map(x -> CriteriaRequest.resolve(x).withRegexOptions("i"))
                        .filter(CriteriaRequest::isValid)
                        .collect(Collectors.toList()));
        Criteria criteria = q.criteria();
        Assertions.assertEquals(criteria, Criteria.where("age").gt(10));
    }

    @Test
    public void testLt() throws CriteriaQueryLogicException, CriteriaOperationNotSupported {
        List<String> queries = new LinkedList<>();
        queries.add("age<10");
        QueryExecutor<?> q = new QueryExecutor<>(null, null)
                .queries(queries.stream()
                        .map(x -> CriteriaRequest.resolve(x).withRegexOptions("i"))
                        .filter(CriteriaRequest::isValid)
                        .collect(Collectors.toList()));
        Criteria criteria = q.criteria();
        Assertions.assertEquals(criteria, Criteria.where("age").lt(10));
    }

    @Test
    public void testGte() throws CriteriaQueryLogicException, CriteriaOperationNotSupported {
        List<String> queries = new LinkedList<>();
        queries.add("age>=10");
        QueryExecutor<?> q = new QueryExecutor<>(null, null)
                .queries(queries.stream()
                        .map(x -> CriteriaRequest.resolve(x).withRegexOptions("i"))
                        .filter(CriteriaRequest::isValid)
                        .collect(Collectors.toList()));
        Criteria criteria = q.criteria();
        Assertions.assertEquals(criteria, Criteria.where("age").gte(10));
    }

    @Test
    public void testLte() throws CriteriaQueryLogicException, CriteriaOperationNotSupported {
        List<String> queries = new LinkedList<>();
        queries.add("age<=10");
        QueryExecutor<?> q = new QueryExecutor<>(null, null)
                .queries(queries.stream()
                        .map(x -> CriteriaRequest.resolve(x).withRegexOptions("i"))
                        .filter(CriteriaRequest::isValid)
                        .collect(Collectors.toList()));
        Criteria criteria = q.criteria();
        Assertions.assertEquals(criteria, Criteria.where("age").lte(10));
    }

    @Test
    public void testIn() throws CriteriaQueryLogicException, CriteriaOperationNotSupported {
        List<String> queries = new LinkedList<>();
        queries.add("ids#=1;2;3");
        QueryExecutor<?> q = new QueryExecutor<>(null, null)
                .queries(queries.stream()
                        .map(x -> CriteriaRequest.resolve(x).withRegexOptions("i"))
                        .filter(CriteriaRequest::isValid)
                        .collect(Collectors.toList()));
        Criteria criteria = q.criteria();
        Assertions.assertEquals(criteria, Criteria.where("ids").in(1, 2, 3));
    }
}
