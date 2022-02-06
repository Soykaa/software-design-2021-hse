package ru.hse.software.design;

import java.util.Scanner;

/**
 * Contains custom realization of command-line interpreter for bash commands.
 * Details about accepted commands are described in README.md.
 */

public class CLI {
    private boolean isRunning = false;

    /**
     * Starts CLI interpreter. Input arguments can be passed, but they are not required and won't be used.
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
     * Launches the application and waits for user input in a blocking mode.
     * Each command is accepted as a single line.
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
     * Tells the information about the status of the application (is running or not).
     *
     * @return True if the application is running, false if it is not
     **/
    public boolean isRunning() {
        return isRunning;
    }
}
