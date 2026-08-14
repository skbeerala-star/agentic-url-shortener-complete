package com.example.urlshortener.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Base62Test {
    @Test
    void encodesZero() {
        assertEquals("0", Base62.encode(0));
    }

    @Test
    void encodesPositiveValue() {
        assertEquals("10", Base62.encode(62));
    }
}
