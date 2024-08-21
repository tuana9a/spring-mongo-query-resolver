package com.tuana9a.spring.mongo.qrisolver;

import com.tuana9a.spring.mongo.qrisolver.resolvers.PageableResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageableResolverTests {
    @Test
    public void test1() {
        Pageable pageable = PageableResolver.resolve(0, 10);
        Pageable desiredPageable = PageRequest.of(0, 10);
        Assertions.assertEquals(pageable, desiredPageable);
    }

    @Test
    public void test2() {
        Pageable pageable = PageableResolver.resolve(1234, 0);
        Pageable desiredPageable = Pageable.unpaged();
        Assertions.assertEquals(pageable, desiredPageable);
    }

    @Test
    public void test3() {
        Pageable pageable = PageableResolver.resolve(1234, -10);
        Pageable desiredPageable = Pageable.unpaged();
        Assertions.assertEquals(pageable, desiredPageable);
    }
}
