package commands;

import command_management.CommandManager;
import net.client.Client;
import net.data.Request;
import net.data.ServerCommand;
import worker.PersonStorage;

import java.io.BufferedReader;
import java.util.List;
import java.util.logging.Logger;

import static worker.PersonStorage.getPersons;

/**
 * class-command which remove person with entered id
 */
public class RemoveById implements Command {
    private static Logger logger = Logger.getLogger("lab6");

    /**
     * @param line    command from main menu
     * @param in      parameter for input
     * @param manager command manager for commands menu
     */
    @Override
    public void execute(List<String> line, BufferedReader in, CommandManager manager) {
        if (line.size() != 2) {
            System.err.println("Error in command");
            return;
        }

        try {
            var command = ServerCommand.removeById(Integer.parseInt(line.get(1)));
            Client.getClient().sendRequest(new Request(command));
            if (manager.isFromFile) Thread.sleep(50);
            System.out.println("----- Deletion completed -----");
        } catch (Exception exc) {
            System.err.println("Error in finding person" + exc.getMessage());
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
