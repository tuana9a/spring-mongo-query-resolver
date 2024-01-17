package com.tuana9a.spring.data.mongodb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

public class SortBuilderTests {
    @Test
    public void testAsc() {
        Sort sort = SortBuilder.from("age=1").toSort();
        Assertions.assertEquals(sort, Sort.by(Sort.Direction.ASC, "age"));
    }

    @Test
    public void testDesc() {
        Sort sort = SortBuilder.from("age=-1").toSort();
        Assertions.assertEquals(sort, Sort.by(Sort.Direction.DESC, "age"));
    }

    @Test
    public void testPattern() {
        Sort sort = SortBuilder.from("age==1").toSort();
        Assertions.assertEquals(sort, Sort.by(Sort.Direction.ASC, "age"));
    }
}
