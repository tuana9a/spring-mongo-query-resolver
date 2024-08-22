package com.tuana9a.spring.mongo.qrisolver;

import com.tuana9a.spring.mongo.qrisolver.errors.Error;
import com.tuana9a.spring.mongo.qrisolver.resolvers.SortResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

public class SortResolverTests {
    @Test
    public void testEmptySort() throws Error {
        Sort sort = SortResolver.resolve("");
        Sort desiredSort = Sort.unsorted();
        Assertions.assertEquals(sort, desiredSort);
    }

    @Test
    public void testBasicUsage() throws Error {
        Sort sort = SortResolver.resolve("age=-1,address=1");
        // this will not work
        // Sort desiredSort = Sort.by(Sort.Direction.ASC, "address").and(Sort.by(Sort.Direction.DESC, "address"));
        Sort desiredSort = Sort.by(Sort.Direction.DESC, "age").and(Sort.by(Sort.Direction.ASC, "address"));
        Assertions.assertEquals(sort, desiredSort);
    }
}
