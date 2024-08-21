package com.tuana9a.spring.mongo.qrisolver.resolvers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageableResolver {
    public static Pageable resolve(int page, int size) {
        return size <= 0 ? Pageable.unpaged() : PageRequest.of(page, size);
    }
}
