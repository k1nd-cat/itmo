package commands;

import command_management.CommandManager;
import models.*;
import net.client.Client;
import net.data.Request;
import net.data.ServerCommand;
import utils.PersonCreator;

import java.io.BufferedReader;
import java.util.List;

import static utils.PersonCreator.createHeight;
import static utils.PersonCreator.createName;

/**
 * class-command add person if his height is max
 */
public class AddIfMax implements Command{
    /**
     * @param line    command from main menu
     * @param in      parameter for input
     * @param manager command manager for commands menu
     */
    @Override
    public void execute(List<String> line, BufferedReader in, CommandManager manager) {
        if (line.size() != 1) {
            System.err.println("Error in command");
            return;
        }

        try {
            Person newPerson;
            var name = createName(in, manager);
            long height = createHeight(in, manager);

            var command = ServerCommand.getLast();
            var person = Client.getClient().sendRequest(new Request(command)).getBody(Person.class);
            if (person == null || height <= person.getHeight()) return;
            var otherParameters = PersonCreator.halfPersonCreation(in, manager);
            newPerson = new Person(name, height, otherParameters);

            command = ServerCommand.add(newPerson);
            Client.getClient().sendRequest(new Request(command));
            if (manager.isFromFile) Thread.sleep(50);

            System.out.println("----- Man added to the collection -----");
        } catch (Exception exc) {
            System.err.println("Error in creating person\nproblem: " + exc.getMessage());
            if (manager.isFromFile) {
                manager.shouldExit = true;
            }
        }
    }

    /**
     * @return description for this command
     */
    @Override
    public String description() {
        return "add_if_max {element}    Add new person in collection if person's height is highest";
    }
}
