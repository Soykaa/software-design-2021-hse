package ru.hse.software.design;

import java.util.Scanner;

public class CLI {
    private boolean isRunning = false;

    public static void main(String[] args) {
        System.out.println("-----CLI interpreter-----");
        CLI instance = new CLI();
        instance.start();
    }

    public void start() {
        isRunning = true;
        Path path = new Path(System.getenv("PATH").split(":"));
        Scanner userInput = new Scanner(System.in);
        Executor executor = new Executor(path);
        while (isRunning) {
            System.out.println("$");
            String command = userInput.nextLine();
            if (!command.isEmpty()) {
                try {
                    executor.execute(command);
                } catch (Exception e) {
                    System.out.println("Command execution failed with exception " + e.getMessage());
                }
            }
        }
    }

    public void exit() {
        isRunning = false;
        System.out.println("Exiting CLI interpreter");
    }
}
