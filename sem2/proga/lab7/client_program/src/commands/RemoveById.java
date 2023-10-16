package commands;

import command_management.CommandManager;
import models.Person;
import net.client.Client;
import net.data.Request;
import net.data.Response;
import net.data.ServerCommand;
import users.Identification;
import utils.PersonCreator;
import worker.PersonStorage;

import java.io.BufferedReader;
import java.util.List;

import static worker.PersonStorage.getPersons;

/**
 * class-command which remove person with entered id
 */
public class RemoveById implements Command {
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
            var command = ServerCommand.findById(Integer.parseInt(line.get(1)));
            Response response = Client.getClient().sendRequest(new Request(command));
            Person currentPerson = response.getBody(Person.class);
            if (currentPerson == null) {
                System.out.println("ERROR: person is not found");
                return;
            }
            int userId = Identification.getIdentification().getUserInfo().getId();
            if (!currentPerson.getUserId().equals(userId)) {
                System.out.println("ACCESS DENIED: you don't have permissions");
                return;
            }
            command = ServerCommand.remove(currentPerson.getId());
            Client.getClient().sendRequest(new Request(command));

            System.out.println("----- The person was successfully removed -----");
        } catch (Exception exc) {
            System.err.println("Error in remove person " + exc.getMessage());
        }
    }

    /**
     * @return this command description
     */
    @Override
    public String description() {
        return "remove_by_id    Delete person from collection by id";
    }
}
