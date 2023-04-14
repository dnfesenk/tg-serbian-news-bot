package com.denisfesenko.util;

public class StringUtils {

    public static String escapeString(String s) {
        return s.replace(".", "\\.").replace("(", "\\(").replace(")", "\\)")
                .replace("-", "\\-").replace(">", "\\>").replace("<", "\\<");
    }

    private StringUtils() {
    }
}
