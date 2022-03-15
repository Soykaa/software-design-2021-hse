package ru.hse.software.design;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class responsible for managing environment variables.
 **/
public class Environment {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Map<String, String> environmentVariables = new HashMap<>();
    private static Path currentFolderPath = Paths.get(System.getProperty("user.dir"));

    /**
     * Saves the name of a variable and its value.
     * If the name already exists, overwrites the value.
     *
     * @param variable variable name
     * @param value    variable value
     **/
    public static void set(String variable, String value) {
        try {
            lock.lock();
            environmentVariables.put(variable, value);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns the variable value.
     *
     * @param variable variable name
     * @return Variable value as Optional
     **/
    public static Optional<String> get(String variable) {
        try {
            lock.lock();
            if (!environmentVariables.containsKey(variable)) {
                return Optional.empty();
            }
            return Optional.of(environmentVariables.get(variable));
        } finally {
            lock.unlock();
        }
    }


    /**
     * Returns all the environment variables.
     *
     * @return Environment variables and its values as map
     */
    public static Map<String, String> getAll() {
        try {
            lock.lock();
            return environmentVariables;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Remove all environment variables.
     **/
    public static void clear() {
        try {
            lock.lock();
            environmentVariables.clear();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Return path of current working directory
     */
    public static Path getCurrentFolderPath() {
        return currentFolderPath;
    }

    /**
     * @param path where we go from this directory
     */
    public static void setCurrentFolderPath(String path) {
        currentFolderPath = getRelativePath(path);
    }

    /**
     * set current directory to user home folder
     */
    public static void setCurrentFolderPath() {
        currentFolderPath = Paths.get(System.getProperty("user.home"));
    }

    /**
     * @param path path relatively to current working directory
     * @return result path
     */
    public static Path getRelativePath(String path) {
        if (Paths.get(path).isAbsolute()) {
            return Paths.get(path);
        } else {
            return Paths.get(currentFolderPath.toAbsolutePath().toString(), path);
        }
    }
}
