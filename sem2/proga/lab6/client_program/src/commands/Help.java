package commands;

import command_management.CommandManager;

import java.io.BufferedReader;
import java.util.List;

/**
 * class-command which print all command
 */
public class Help implements Command {
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

        for (Command c: CommandManager.getCommands().values())
            System.out.println(c.description());
    }

    /**
     * @return this command description
     */
    @Override
    public String description() {
        return "help    Info about all commands";
    }
}
