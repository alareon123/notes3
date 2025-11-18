package com.example.notes.core;

import org.junit.jupiter.api.BeforeAll;

public class BaseApiTest {

    @BeforeAll
    public static void setUp() {
        Specs.setupRequestSpec();
    }
}
