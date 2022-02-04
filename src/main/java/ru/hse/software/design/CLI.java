package ru.hse.software.design;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class CLI {
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    public static void main(String[] args) {
        System.out.println("-----CLI interpreter-----");
        CLI instance = new CLI();
        instance.start();
    }

    private Executor createExecutor() {
        return new Executor(this);
    }

    public void start() {
        isRunning.set(true);
        Scanner userInput = new Scanner(System.in);
        userInput.useDelimiter(System.lineSeparator());
        Executor executor = createExecutor();
        while (isRunning.get()) {
            System.out.print("$ ");
            if (userInput.hasNextLine()) {
                String command = userInput.next();
                if (!command.isEmpty()) {
                    try {
                        executor.execute(command);
                    } catch (Exception e) {
                        System.out.println("FAILURE: Command execution failed with exception " + e);
                    }
                }
            }
        }
    }

    public void exit() {
        isRunning.set(false);
        System.out.println("-----Exiting CLI interpreter-----");
    }

    public boolean isRunning() {
        return isRunning.get();
    }
}
