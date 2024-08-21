package com.tuana9a.spring.mongo.qrisolver.resolvers;

import com.tuana9a.spring.mongo.qrisolver.configs.Config;
import com.tuana9a.spring.mongo.qrisolver.configs.OpHandlerTable;
import com.tuana9a.spring.mongo.qrisolver.errors.Error;
import com.tuana9a.spring.mongo.qrisolver.payloads.CriteriaPart;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.*;

import static java.util.Objects.isNull;

public class CriteriaResolver {
    public static Criteria resolve(String input) throws Error {
        List<CriteriaPart> parts = new LinkedList<>();
        for (String x : input.split(Config.DELIMITER)) {
            parts.add(CriteriaPartResolver.resolve(x));
        }
        OpHandlerTable opHandlerTable = OpHandlerTable.getInstance();
        Map<String, Queue<CriteriaPart>> criteriaPartTable = new HashMap<>();
        for (CriteriaPart part : parts) {
            String key = part.getKey();
            Queue<CriteriaPart> existed = criteriaPartTable.get(key);
            if (!isNull(existed)) {
                existed.offer(part);
                continue;
            }
            Queue<CriteriaPart> queue = new LinkedList<>();
            queue.add(part);
            criteriaPartTable.put(key, queue);
        }
        Criteria criteria = new Criteria();
        for (Queue<CriteriaPart> queue : criteriaPartTable.values()) {
            CriteriaPart firstOne = queue.poll();
            String firstOneOperator = firstOne.getOperator();
            criteria = opHandlerTable.get(firstOneOperator).first.process(criteria, firstOne);
            CriteriaPart nextOne = queue.poll();
            while (!isNull(nextOne)) {
                String nextOneOperator = nextOne.getOperator();
                criteria = opHandlerTable.get(nextOneOperator).chain.process(criteria, nextOne);
                nextOne = queue.poll();
            }
        }
        return criteria;
    }
}
