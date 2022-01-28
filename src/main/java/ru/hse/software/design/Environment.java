package ru.hse.software.design;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class Environment {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Map<String, String> envVariables = new HashMap<>();

    public static void set(String variable, String value) {
        try {
            lock.lock();
            envVariables.put(variable, value);
        } finally {
            lock.unlock();
        }
    }

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

    public static String[] getAll() {
        try {
            lock.lock();
            String[] envp = new String[envVariables.size()];
            int currentIndex = 0;
            for (var entry : envVariables.entrySet()) {
                envp[currentIndex] = entry.getKey() + "=" + entry.getValue();
            }
            return envp;
        } finally {
            lock.unlock();
        }
    }
}
