package ru.hse.software.design;

import java.util.Scanner;

/**
 * Class that the user is interacting with.
 * Internally, it stores the 'isRunning' flag as a private field,
 * which is set to true when the application is running and false when it terminates.
 * It has two main public methods 'start' and 'exit'
 * and private method 'createExecutor' responsible for creating an object of the Executor class.
 */

public class CLI {
    private boolean isRunning = false;

    /**
     * Creates CLI instance and calls its method 'start'.
     *
     * @param args input args
     **/
    public static void main(String[] args) {
        System.out.println("-----CLI interpreter-----");
        CLI instance = new CLI();
        instance.start();
    }

    private Executor createExecutor() {
        return new Executor(this);
    }

    /**
     * Launches the application.
     * Accepts user commands and executes them.
     * In case of an error displays the appropriate message.
     **/
    public void start() {
        isRunning = true;
        Scanner userInput = new Scanner(System.in);
        userInput.useDelimiter(System.lineSeparator());
        Executor executor = createExecutor();
        while (isRunning) {
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

    /**
     * Terminates the application.
     **/
    public void exit() {
        isRunning = false;
        System.out.println("-----Exiting CLI interpreter-----");
    }

    /**
     * Returns the status of the application (is running or not).
     *
     * @return true if the application is running, false if is not
     **/
    public boolean isRunning() {
        return isRunning;
    }
}
