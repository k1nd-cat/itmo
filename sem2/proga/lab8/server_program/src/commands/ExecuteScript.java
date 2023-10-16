package commands;

import command_management.CommandManager;
import exceptions.RecursionFileException;
import json_control.FileDespatcher;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;
import java.util.Objects;

/**
 * class-command read script from file
 */
public class ExecuteScript implements Command {
    /**
     * @param line    command from main menu
     * @param in      parameter for input
     * @param manager command manager for commands menu
     */
    @Override
    public void execute(List<String> line, BufferedReader in, CommandManager manager) {
        if (line.size() != 2) {
            System.err.println("Error in command syntax");
            return;
        }

        try {
            if (CommandManager.getScriptFiles().contains(line.get(1))) throw new RecursionFileException();
            CommandManager.getScriptFiles().add(line.get(1));
            new CommandManager().readCommand(new BufferedReader(new StringReader(Objects.requireNonNull(FileDespatcher.readFile(line.get(1))))), true);
            CommandManager.getScriptFiles().remove(line.get(1));
            System.out.println("----- Reading file is finished -----");
        } catch (StackOverflowError | NullPointerException | RecursionFileException exc) {
            System.err.println("Error in file: " + exc.getMessage());
        }
    }

    /**
     * @return this command description
     */
    @Override
    public String description() {
        return "execute_script file_name    Read script from file";
    }
}
