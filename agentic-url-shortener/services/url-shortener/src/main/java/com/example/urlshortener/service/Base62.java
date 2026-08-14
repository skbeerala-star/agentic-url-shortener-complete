package com.example.urlshortener.service;

public final class Base62 {
    private static final char[] ALPHABET =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private Base62() {}

    public static String encode(long value) {
        if (value == 0) return "0";
        StringBuilder out = new StringBuilder();
        while (value > 0) {
            out.append(ALPHABET[(int)(value % 62)]);
            value /= 62;
        }
        return out.reverse().toString();
    }
}
