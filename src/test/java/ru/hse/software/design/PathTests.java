package ru.hse.software.design;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PathTests {
    @Test
    public void testSetPath() {
        var path = new Path(new String[]{});
        path.setPath("usr/bin");
        Assertions.assertEquals(1, path.getPaths().size());
        Assertions.assertTrue(path.getPaths().contains("usr/bin"));
    }

    @Test
    public void testGetPaths() {
        var path = new Path(new String[]{"Users/harrypotter/Documents", "usr/bin", "Users/lizaBenneth"});
        Assertions.assertEquals(3, path.getPaths().size());
        Assertions.assertTrue(path.getPaths().contains("Users/harrypotter/Documents"));
        Assertions.assertTrue(path.getPaths().contains("usr/bin"));
        Assertions.assertTrue(path.getPaths().contains("Users/lizaBenneth"));
    }
}