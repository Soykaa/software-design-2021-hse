package ru.hse.software.design;

import java.util.Scanner;

public class CLI {
    private boolean isRunning = false;

    public static void main(String[] args) {
        System.out.println("-----CLI interpreter-----");
        CLI instance = new CLI();
        instance.start();
    }

    private Executor createExecutor() {
        Path path = new Path(System.getenv("PATH").split(":"));
        return new Executor(path);
    }

    public void start() {
        isRunning = true;
        Scanner userInput = new Scanner(System.in);
        userInput.useDelimiter(System.lineSeparator());
        Executor executor = createExecutor();
        Thread thread = new Thread(() -> {
            while (isRunning) {
                if (userInput.hasNextLine()) {
                    String command = userInput.next();
                    System.out.println(command);
                    if (!command.isEmpty()) {
                        try {
                            executor.execute(command);
                        } catch (Exception e) {
                            System.out.println("FAILURE: Command execution failed with exception " + e.getMessage());
                        }
                    }
                }
            }
        });
        thread.start();
    }

    public void exit() {
        isRunning = false;
        System.out.println("-----Exiting CLI interpreter-----");
    }

    public boolean isRunning() {
        return isRunning;
    }
}
