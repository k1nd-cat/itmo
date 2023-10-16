package commands;

import command_management.CommandManager;
import models.*;
import net.client.Client;
import net.data.Request;
import net.data.ServerCommand;
import users.Identification;
import utils.PersonCreator;

import java.io.BufferedReader;
import java.util.List;
import java.util.logging.Logger;

/**
 * class-command add element
 */
public class Add implements Command {
    private static Logger logger = Logger.getLogger("lab6");

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
            var name = PersonCreator.createName(in, manager);
            long height = PersonCreator.createHeight(in, manager);

            var otherParameters = PersonCreator.halfPersonCreation(in, manager);
            var newPerson = new Person(name, height, otherParameters);
            newPerson.setUserId(Identification.getIdentification().getUserInfo().getId());

            var command = ServerCommand.add(newPerson);
            Client.getClient().sendRequest(new Request(command));

            System.out.println("----- Man added to the collection -----");
            if (manager.isFromFile) Thread.sleep(100);
        } catch (Exception exc) {
            System.err.println("Error in creating person\nproblem: " + exc.getMessage());
            if (manager.isFromFile) {
                manager.shouldExit = true;
            }
        }
    }

    /**
     * @return description of this command
     */
    @Override
    public String description() {
        return "add {name height}   Add new Person in collection";
    }
}
