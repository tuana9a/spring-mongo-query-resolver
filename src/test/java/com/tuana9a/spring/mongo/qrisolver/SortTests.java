package com.tuana9a.spring.mongo.qrisolver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

public class SortTests {
    @Test
    public void canNotChainLikeThis() {
        Sort sort = Sort.unsorted();
        sort.and(Sort.by(Sort.Direction.ASC, "age"));
        sort.and(Sort.by(Sort.Direction.DESC, "name"));
        Assertions.assertEquals(sort, Sort.unsorted()); // this is ridiculous
        Assertions.assertNotEquals(sort, Sort.by(Sort.Direction.ASC, "age").and(Sort.by(Sort.Direction.DESC, "name")));
    }

    @Test
    public void mustReAssignItToWork() {
        Sort sort = Sort.unsorted();
        sort = sort.and(Sort.by(Sort.Direction.ASC, "age"));
        sort = sort.and(Sort.by(Sort.Direction.DESC, "name"));
        Assertions.assertEquals(sort, Sort.by(Sort.Direction.ASC, "age").and(Sort.by(Sort.Direction.DESC, "name")));
    }
}
