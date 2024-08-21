package com.tuana9a.spring.mongo.qrisolver.resolvers;

import org.bson.types.ObjectId;

public class ValueResolver {
    public static Object resolve(String input) {
        try {
            if (input == null) return null;
            if (input.matches("^\\s$")) return null;
            if (input.matches("^-*\\d{1,10}$")) return Integer.parseInt(input);
            if (input.matches("^-*\\d{10,20}$")) return Long.parseLong(input);
            if (ObjectId.isValid(input)) return new ObjectId(input);
            if (input.equals("undefined") || input.equals("null")) return null;
            if (input.equals("true")) return true;
            if (input.equals("false")) return false;
        } catch (Exception ignored) {
            // do nothing
        }
        return input;
    }
}
