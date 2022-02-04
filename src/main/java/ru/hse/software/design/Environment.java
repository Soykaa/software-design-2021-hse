package ru.hse.software.design;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class responsible for storing environment variables.
 * Contains lock to work with threads and map of environment variables
 * as a private fields.
 * Also contains several static public methods.
 **/
public class Environment {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Map<String, String> envVariables = new HashMap<>();

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
            envVariables.put(variable, value);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns the variable value.
     *
     * @param variable variable name
     * @return variable value as Optional
     **/
    public static Optional<String> get(String variable) {
        try {
            lock.lock();
            if (!envVariables.containsKey(variable)) {
                return Optional.empty();
            }
            return Optional.of(envVariables.get(variable));
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns all environment variables with its values.
     *
     * @return environment variables and its values as string array
     **/
    public static String[] getAll() {
        try {
            lock.lock();
            String[] envp = new String[envVariables.size()];
            int currentIndex = 0;
            for (var entry : envVariables.entrySet()) {
                envp[currentIndex] = entry.getKey() + "=" + entry.getValue();
                currentIndex++;
            }
            return envp;
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
            envVariables.clear();
        } finally {
            lock.unlock();
        }
    }
}
