package commands;

import command_management.CommandManager;
import models.Person;
import net.client.Client;
import net.data.Request;
import net.data.Response;
import net.data.ServerCommand;
import utils.PersonCreator;
import worker.PersonStorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 * class-command for update person info
 */
public class Update implements Command {
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

            System.out.println("Enter name: ");
            String name = in.readLine();
            System.out.println("Enter height" + ": ");
            long height = Long.parseLong(in.readLine());
            var otherParameters = PersonCreator.halfPersonCreation(in, manager);

            currentPerson.setName(name);
            currentPerson.setHeight(height);
            currentPerson.setCoordinates(otherParameters.coordinates);
            currentPerson.setEyeColor(otherParameters.eyeColor);
            currentPerson.setHairColor(otherParameters.hairColor);
            currentPerson.setNationality(otherParameters.nationality);
            currentPerson.setLocation(otherParameters.location);

            command = ServerCommand.update(currentPerson);
            Client.getClient().sendRequest(new Request(command));

            System.out.println("----- The person was successfully updated -----");
        } catch (Exception exc) {
            System.err.println("Error in update person " + exc.getMessage());
        }
    }

    /**
     * @return this command description
     */
    @Override
    public String description() {
        return "update id   Update info about person by id";
    }
}
