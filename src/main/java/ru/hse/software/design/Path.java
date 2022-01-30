package ru.hse.software.design;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Path {
    private final List<String> paths = new ArrayList<>();
    public void setPath(String absoluteDirectoryPath) {
        paths.add(absoluteDirectoryPath);
    }
    public List<String> getPaths() {
        return new ArrayList<>(paths);
    }

    public Path(String[] absoluteDirectoryPaths) {
        paths.addAll(Arrays.asList(absoluteDirectoryPaths));
    }
}
