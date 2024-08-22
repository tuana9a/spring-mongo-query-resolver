package com.tuana9a.spring.mongo.qrisolver;

import com.tuana9a.spring.mongo.qrisolver.resolvers.CriteriaResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.InvalidMongoDbApiUsageException;

public class AdvanceCriteriaResolverTests {
    @Test
    public void repeatEqOperator() {
        String q = "age==5,age==10";
        Assertions.assertThrows(InvalidMongoDbApiUsageException.class, () -> CriteriaResolver.resolve(q));
    }
}
