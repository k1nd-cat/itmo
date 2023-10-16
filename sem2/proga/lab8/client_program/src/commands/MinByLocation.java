package commands;

import command_management.CommandManager;
import models.Person;
import worker.PersonStorage;

import java.io.BufferedReader;
import java.util.Comparator;
import java.util.List;

/**
 * class command which find and print person with min location value
 */
public class MinByLocation implements Command {
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

        try {
            System.out.println(PersonStorage.getPersons().stream().min(Comparator.comparing(Person::getLocation)).orElseThrow());
        } catch (RuntimeException exc) {
            System.err.println("Query execution error: " + exc.getMessage());
        }
    }

    /**
     * @return this command description
     */
    @Override
    public String description() {
        return "Display the person with a minimum value of location";
    }
}
