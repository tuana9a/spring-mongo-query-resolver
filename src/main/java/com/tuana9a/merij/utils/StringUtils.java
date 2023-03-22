package com.tuana9a.merij.utils;

import org.bson.types.ObjectId;

public class StringUtils {
    public static Object auto(String input) {
        try {
            if (input == null) return null;
            if (input.matches("^\\s$")) return null;
            if (input.equals("undefined")) return null;
            if (input.equals("null")) return null;
            if (input.equals("true")) return true;
            if (input.equals("false")) return false;
            if (ObjectId.isValid(input)) return new ObjectId(input);
            if (input.matches("^\\d{1,10}$")) return Integer.parseInt(input);
            if (input.matches("^\\d{10,20}$")) return Long.parseLong(input);
        } catch (Exception ignored) {
            // do nothing
        }
        return input;
    }
}
