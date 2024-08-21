package com.tuana9a.spring.mongo.qrisolver.processors;

import com.tuana9a.spring.mongo.qrisolver.payloads.CriteriaPart;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 * org.springframework.data.mongodb.core.query.Criteria has a weird way to build up the "criteria query"
 * filter that has same query key must be chained right after the criteria
 * Ex: Criteria.where("age").lt(50).and("name").contains("Alice").and("age").gt(10) will through error
 * Ex: Criteria.where("age").lt(50).gt(10).and("name").contains("Alice") will go through
 */
public interface OpProcessor {
    Criteria process(Criteria criteria, CriteriaPart part);
}
