package ru.hse.software.design;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EnvironmentTests {
    @BeforeEach
    public void setUp() {
        Environment.clear();
    }

    @Test
    public void testSet() {
        Environment.set("SPANISH_GREETING", "HOLA");
        Assertions.assertTrue(Environment.get("SPANISH_GREETING").isPresent());
    }

    @Test
    public void testGet() {
        Environment.set("SPANISH_GREETING", "HOLA");
        Assertions.assertTrue(Environment.get("SPANISH_GREETING").isPresent());
        Assertions.assertEquals("HOLA", Environment.get("SPANISH_GREETING").get());
    }

    @Test
    public void testGetEmpty() {
        Assertions.assertFalse(Environment.get("SPANISH_GREETING").isPresent());
    }

    @Test
    public void testGetAll() {
        Environment.set("SPANISH_GREETING", "HOLA");
        Environment.set("SWEDISH_GREETING", "HEJ");
        Environment.set("ENGLISH_GREETING", "HELLO");
        Map<String, String> allEnvPairs = Environment.getAll();
        Assertions.assertEquals("HOLA", allEnvPairs.get("SPANISH_GREETING"));
        Assertions.assertEquals("HEJ", allEnvPairs.get("SWEDISH_GREETING"));
        Assertions.assertEquals("HELLO", allEnvPairs.get("ENGLISH_GREETING"));
    }
}
