package com.tuana9a.spring.mongo.qrisolver.payloads;

import com.tuana9a.spring.mongo.qrisolver.processors.OpProcessor;

public class OpHandler {
    public final OpProcessor first;
    public final OpProcessor chain;

    public OpHandler(OpProcessor first, OpProcessor chain) {
        this.first = first;
        this.chain = chain;
    }
}
