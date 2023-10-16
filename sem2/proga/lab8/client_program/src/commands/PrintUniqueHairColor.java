package commands;

import command_management.CommandManager;
import models.Person;
import worker.PersonStorage;

import java.io.BufferedReader;
import java.util.List;

/**
 * class-command witch print unique hair color
 */
public class PrintUniqueHairColor implements Command {
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
            PersonStorage.getPersons().stream().map(Person::getHairColor).distinct().forEach(System.out::println);
        } catch (RuntimeException exc) {
            System.err.println("Error in unique hair color" + exc.getMessage());
        }
    }

    /**
     * @return this command description
     */
    @Override
    public String description() {
        return "Display unique values of the hair color of all persons in the collection";
    }
}
