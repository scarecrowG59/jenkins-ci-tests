package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppUnitTest {

    @Test
    public void testAddMethod() {
        int result = App.add(2, 3);
        assertEquals(5, result, "Addition method should return correct sum");
    }
}

