package com.tuana9a.spring.mongo.qrisolver;

import com.tuana9a.spring.mongo.qrisolver.configs.SupportedOperator;
import com.tuana9a.spring.mongo.qrisolver.errors.Error;
import com.tuana9a.spring.mongo.qrisolver.payloads.CriteriaPart;
import com.tuana9a.spring.mongo.qrisolver.resolvers.CriteriaPartResolver;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class CriteriaPartResolverTests {
    @Test
    public void testIs_String() throws Error {
        CriteriaPart part = CriteriaPartResolver.resolve("name==tuana9a");
        Assertions.assertEquals(part.getKey(), "name");
        Assertions.assertEquals(part.getOperator(), SupportedOperator.EQ);
        Assertions.assertEquals(part.getValue(), "tuana9a");
    }

    @Test
    public void testIs_ObjectId() throws Error {
        ObjectId id = new ObjectId();
        CriteriaPart part = CriteriaPartResolver.resolve("id==" + id);
        Assertions.assertEquals(part.getKey(), "id");
        Assertions.assertEquals(part.getOperator(), SupportedOperator.EQ);
        Assertions.assertEquals(part.getValue(), id);
    }

    @Test
    public void testIs_Null() throws Error {
        CriteriaPart part = CriteriaPartResolver.resolve("id==null");
        Assertions.assertEquals(part.getKey(), "id");
        Assertions.assertEquals(part.getOperator(), SupportedOperator.EQ);
        Assertions.assertEquals(part.getValue(), null);
    }

    @Test
    public void testIs_Undefined() throws Error {
        CriteriaPart part = CriteriaPartResolver.resolve("id==undefined");
        Assertions.assertEquals(part.getKey(), "id");
        Assertions.assertEquals(part.getOperator(), SupportedOperator.EQ);
        Assertions.assertEquals(part.getValue(), null);
    }

    @Test
    public void testIs_True() throws Error {
        CriteriaPart part = CriteriaPartResolver.resolve("enabled==true");
        Assertions.assertEquals(part.getKey(), "enabled");
        Assertions.assertEquals(part.getOperator(), SupportedOperator.EQ);
        Assertions.assertEquals(part.getValue(), true);
    }

    @Test
    public void testIs_False() throws Error {
        CriteriaPart part = CriteriaPartResolver.resolve("deleted==false");
        Assertions.assertEquals(part.getKey(), "deleted");
        Assertions.assertEquals(part.getOperator(), SupportedOperator.EQ);
        Assertions.assertEquals(part.getValue(), false);
    }

    @Test
    public void testNe_String() throws Error {
        CriteriaPart part = CriteriaPartResolver.resolve("name!=tuana9a");
        Assertions.assertEquals(part.getKey(), "name");
        Assertions.assertEquals(part.getOperator(), SupportedOperator.NE);
        Assertions.assertEquals(part.getValue(), "tuana9a");
    }

    @Test
    public void testNe_PositiveInt() throws Error {
        CriteriaPart part = CriteriaPartResolver.resolve("age!=18");
        Assertions.assertEquals(part.getKey(), "age");
        Assertions.assertEquals(part.getOperator(), SupportedOperator.NE);
        Assertions.assertEquals(part.getValue(), 18);
    }

    @Test
    public void testNe_NegativeInt() throws Error {
        CriteriaPart part = CriteriaPartResolver.resolve("age!=-18");
        Assertions.assertEquals(part.getKey(), "age");
        Assertions.assertEquals(part.getOperator(), SupportedOperator.NE);
        Assertions.assertEquals(part.getValue(), -18);
    }

    @Test
    public void testGt_PositiveInt() throws Error {
        CriteriaPart part = CriteriaPartResolver.resolve("age>10");
        Assertions.assertEquals(part.getKey(), "age");
        Assertions.assertEquals(part.getOperator(), SupportedOperator.GT);
        Assertions.assertEquals(part.getValue(), 10);
    }

    @Test
    public void testGt_NegativeInt() throws Error {
        CriteriaPart part = CriteriaPartResolver.resolve("age>-10");
        Assertions.assertEquals(part.getKey(), "age");
        Assertions.assertEquals(part.getOperator(), SupportedOperator.GT);
        Assertions.assertEquals(part.getValue(), -10);
    }

    @Test
    public void testLt_PositiveInt() throws Error {
        CriteriaPart part = CriteriaPartResolver.resolve("age<10");
        Assertions.assertEquals(part.getKey(), "age");
        Assertions.assertEquals(part.getOperator(), SupportedOperator.LT);
        Assertions.assertEquals(part.getValue(), 10);
    }

    @Test
    public void testLt_NegativeInt() throws Error {
        CriteriaPart part = CriteriaPartResolver.resolve("age<-10");
        Assertions.assertEquals(part.getKey(), "age");
        Assertions.assertEquals(part.getOperator(), SupportedOperator.LT);
        Assertions.assertEquals(part.getValue(), -10);
    }

    @Test
    public void testGte_PositiveInt() throws Error {
        CriteriaPart part = CriteriaPartResolver.resolve("age>=10");
        Assertions.assertEquals(part.getKey(), "age");
        Assertions.assertEquals(part.getOperator(), SupportedOperator.GTE);
        Assertions.assertEquals(part.getValue(), 10);
    }

    @Test
    public void testGte_NegativeInt() throws Error {
        CriteriaPart part = CriteriaPartResolver.resolve("age>=-10");
        Assertions.assertEquals(part.getKey(), "age");
        Assertions.assertEquals(part.getOperator(), SupportedOperator.GTE);
        Assertions.assertEquals(part.getValue(), -10);
    }

    @Test
    public void testLte_PositiveInt() throws Error {
        CriteriaPart part = CriteriaPartResolver.resolve("age<=10");
        Assertions.assertEquals(part.getKey(), "age");
        Assertions.assertEquals(part.getOperator(), SupportedOperator.LTE);
        Assertions.assertEquals(part.getValue(), 10);
    }

    @Test
    public void testLte_NegativeInt() throws Error {
        CriteriaPart part = CriteriaPartResolver.resolve("age<=-10");
        Assertions.assertEquals(part.getKey(), "age");
        Assertions.assertEquals(part.getOperator(), SupportedOperator.LTE);
        Assertions.assertEquals(part.getValue(), -10);
    }

    @Test
    public void testIn_PositiveInt() throws Error {
        CriteriaPart part = CriteriaPartResolver.resolve("id@=1;2;3");
        Assertions.assertEquals(part.getKey(), "id");
        Assertions.assertEquals(part.getOperator(), SupportedOperator.IN);
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        expected.add(3);
        Assertions.assertArrayEquals(((ArrayList<?>) part.getValue()).toArray(), expected.toArray());
    }

    @Test
    public void testIn_NegativeInt() throws Error {
        CriteriaPart part = CriteriaPartResolver.resolve("id@=-1;-2;-3");
        Assertions.assertEquals(part.getKey(), "id");
        Assertions.assertEquals(part.getOperator(), SupportedOperator.IN);
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(-1);
        expected.add(-2);
        expected.add(-3);
        Assertions.assertArrayEquals(((ArrayList<?>) part.getValue()).toArray(), expected.toArray());
    }

    @Test
    public void testIn_NegativeLong() throws Error {
        long num1 = -10000000000L;
        long num2 = -20000000000L;
        long num3 = -30000000000L;
        String q = "id@=" + num1 + ";" + num2 + ";" + num3;
        CriteriaPart part = CriteriaPartResolver.resolve(q);
        Assertions.assertEquals(part.getKey(), "id");
        Assertions.assertEquals(part.getOperator(), SupportedOperator.IN);
        ArrayList<Long> expected = new ArrayList<>();
        expected.add(num1);
        expected.add(num2);
        expected.add(num3);
        Assertions.assertArrayEquals(((ArrayList<?>) part.getValue()).toArray(), expected.toArray());
    }
}
