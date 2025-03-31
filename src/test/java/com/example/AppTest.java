package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    public void testAdd() {
        assertEquals(2, App.add(1, 1));
    }

    @Test
    public void testMainMethod() {
        // Это даст Jacoco возможность покрыть main()
        App.main(new String[]{});
    }
}

