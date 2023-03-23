package com.tuana9a.merij;

import com.tuana9a.merij.exceptions.CriteriaOperationNotSupported;
import com.tuana9a.merij.exceptions.CriteriaQueryLogicException;
import com.tuana9a.merij.exceptions.QueryPatternNotMatchException;
import com.tuana9a.merij.requests.CriteriaRequest;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.LinkedList;
import java.util.List;

public class QueryExecutorTests {
    @Test
    public void test() throws QueryPatternNotMatchException, CriteriaQueryLogicException, CriteriaOperationNotSupported {
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
    public void test1() throws QueryPatternNotMatchException, CriteriaQueryLogicException, CriteriaOperationNotSupported {
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
    public void test2() throws QueryPatternNotMatchException, CriteriaQueryLogicException, CriteriaOperationNotSupported {
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
}
