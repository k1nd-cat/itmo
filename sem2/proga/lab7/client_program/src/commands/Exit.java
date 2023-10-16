package commands;

import command_management.CommandManager;

import java.io.BufferedReader;
import java.util.List;

/**
 * class-command which stop running program
 */
public class Exit implements Command {
    /**
     * @param line    command from main menu
     * @param in      parameter for input
     * @param manager command manager for commands menu
     */
    @Override
    public void execute(List<String> line, BufferedReader in, CommandManager manager) {
        if (line.size() != 1) {
            System.err.println("Incorrect command");
            return;
        }

        System.out.println("----- Ending the program -----");
        System.exit(0);
    }

    /**
     * @return this command description
     */
    @Override
    public String description() {
        return "exit    Terminate the program (without saving to a file)";
    }
}
