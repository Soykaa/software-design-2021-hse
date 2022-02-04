package ru.hse.software.design;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class, which stores paths to directories containing external programs.
 * Contains list of paths as private field.
 **/
public class Path {
    private final List<String> paths = new ArrayList<>();

    /**
     * Constructor.
     * Makes paths same as given absolute paths to directories.
     *
     * @param absoluteDirectoryPaths absolute paths to directories
     **/
    public Path(String[] absoluteDirectoryPaths) {
        paths.addAll(Arrays.asList(absoluteDirectoryPaths));
    }

    /**
     * Add absolute path to directory to the list of paths.
     *
     * @param absoluteDirectoryPath absolute path to directory
     **/
    public void setPath(String absoluteDirectoryPath) {
        paths.add(absoluteDirectoryPath);
    }

    /**
     * Returns paths to directories.
     *
     * @return paths as list
     **/
    public List<String> getPaths() {
        return new ArrayList<>(paths);
    }
}
