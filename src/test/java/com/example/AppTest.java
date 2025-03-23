package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {
    @Test
    void testMainRuns() {
        assertDoesNotThrow(() -> App.main(null));
    }
}
