package com.tuana9a.merij;

import com.tuana9a.merij.exceptions.*;
import com.tuana9a.merij.requests.CriteriaRequest;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.LinkedList;
import java.util.List;

public class QueryExecutorTests {
    @Test
    public void test() throws QueryPatternNotMatchException, CriteriaOperationNotSupported {
        List<String> queries = new LinkedList<>();
        queries.add("age>5");
        queries.add("age<10");
        Criteria criteria = new QueryExecutor<>()
                .queries(queries)
                .and(CriteriaRequest.from("age", "!=", 8))
                .criteria();
        Assertions.assertEquals(criteria, Criteria.where("age").gt(5).lt(10).ne(8));
    }

    @Test
    public void test1() throws QueryPatternNotMatchException, CriteriaOperationNotSupported {
        List<String> queries = new LinkedList<>();
        queries.add("age>5");
        queries.add("age<10");
        queries.add("name==tuana9a");
        Criteria criteria = new QueryExecutor<>()
                .queries(queries)
                .and(CriteriaRequest.from("age", "!=", 8))
                .criteria();
        Criteria desiredCriteria = Criteria.where("age").gt(5).lt(10).ne(8)
                .and("name").is("tuana9a");
        Document document = criteria.getCriteriaObject();
        Document desiredDocument = desiredCriteria.getCriteriaObject();
        Assertions.assertEquals(document, desiredDocument);
    }

    @Test
    public void test2() throws QueryPatternNotMatchException, CriteriaOperationNotSupported {
        List<String> queries = new LinkedList<>();
        queries.add("age>5");
        queries.add("age<10");
        queries.add("name==tuana9a");
        queries.add("name*=tuana9a");
        Criteria criteria = new QueryExecutor<>()
                .queries(queries)
                .dropKey("name")
                .and(CriteriaRequest.from("age", "!=", 8))
                .criteria();
        Criteria desiredCriteria = Criteria.where("age").gt(5).lt(10).ne(8);
        Document document = criteria.getCriteriaObject();
        Document desiredDocument = desiredCriteria.getCriteriaObject();
        Assertions.assertEquals(document, desiredDocument);
    }

    @Test
    public void test3() throws QueryPatternNotMatchException, CriteriaOperationNotSupported, SortPatternNotMatchException, SortOperationNotSupported {
        List<String> queries = new LinkedList<>();
        queries.add("age>5");
        queries.add("age<10");
        queries.add("name==tuana9a");
        queries.add("name*=tuana9a");
        List<String> sorts = new LinkedList<>();
        sorts.add("age=-1");
        sorts.add("name=1");
        QueryExecutor<?> queryExecutor = new QueryExecutor<>()
                .queries(queries)
                .dropKey("name")
                .sorts(sorts)
                .and(CriteriaRequest.from("age", "!=", 8));
        Criteria criteria = queryExecutor.criteria();
        Sort sort = queryExecutor.sort();
        Criteria desiredCriteria = Criteria.where("age").gt(5).lt(10).ne(8);
        Document document = criteria.getCriteriaObject();
        Document desiredDocument = desiredCriteria.getCriteriaObject();
        Assertions.assertEquals(document, desiredDocument);
        Assertions.assertEquals(sort, Sort.by(Sort.Direction.DESC, "age").and(Sort.by(Sort.Direction.ASC, "name")));
    }

    @Test
    public void test4() throws QueryPatternNotMatchException, CriteriaOperationNotSupported {
        CriteriaRequest.DEFAULT_REGEX_OPTIONS = "i";
        List<String> queries = new LinkedList<>();
        queries.add("name*=tuana9a");
        QueryExecutor<?> queryExecutor = new QueryExecutor<>().queries(queries);
        Criteria criteria = queryExecutor.criteria();
        Criteria desiredCriteria = Criteria.where("name").regex("tuana9a", "i");
        String document = criteria.getCriteriaObject().toJson();
        String desiredDocument = desiredCriteria.getCriteriaObject().toJson();
        Assertions.assertEquals(document, desiredDocument);
    }
}
